package cloudwaf

import java.sql.Date

import commons.conf.ConfigurationManager
import commons.utils.DateUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @param website_id        网站id
  * @param website_url       网站域名
  * @param target_ip         目的IP
  * @param target_ip_area    被攻击的服务器所属地
  * @param target_ip_country 被攻击的服务器所属国家
  * @param target_ip_city    被攻击的服务器所属城市
  * @param source_ip         攻击源IP
  * @param source_ip_city    攻击IP所属城市
  * @param danger_level      严重等级
  */
object AnalysisStreaming {


  def main(args: Array[String]): Unit = {

    // 构建Spark上下文
    val sparkConf = new SparkConf().setAppName("streamingSystem").setMaster("local[*]")

    // 创建Spark客户端
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(5))

    // 设置检查点目录
        ssc.checkpoint("./streaming_checkpoint")

    // 获取Kafka配置
    val broker_list = ConfigurationManager.config.getString("kafka.broker.list")
    val topics = ConfigurationManager.config.getString("kafka.topics")

    // kafka消费者配置
    val kafkaParam = Map(
      "bootstrap.servers" -> broker_list, //用于初始化链接到集群的地址
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      //用于标识这个消费者属于哪个消费团体
      "group.id" -> "commerce-consumer-group",
      //如果没有初始化偏移量或者当前的偏移量不存在任何服务器上，可以使用这个配置属性
      //可以使用这个配置，latest自动重置偏移量为最新的偏移量
      "auto.offset.reset" -> "latest",
      //如果是true，则这个消费者的偏移量会在后台自动提交
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    // 创建DStream，返回接收到的输入数据
    // LocationStrategies：根据给定的主题和集群地址创建consumer
    // LocationStrategies.PreferConsistent：持续的在所有Executor之间分配分区
    // ConsumerStrategies：选择如何在Driver和Executor上创建和配置Kafka Consumer
    // ConsumerStrategies.Subscribe：订阅一系列主题

    //创建DStream，返回接收到的输入数据

    var dStream = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](Array(topics), kafkaParam))
    dStream.map(s => (s.key(), s.value())).print()

    var adRealTimeValueDStream = dStream.map(consumerRecordRDD => consumerRecordRDD.value())

    // 用于Kafka Stream的线程非安全问题，重新分区切断血统
    adRealTimeValueDStream = adRealTimeValueDStream.repartition(400)

    // 计算攻击实时统计结果(yyyyMMdd_city_websiteurl,trackCount)
    val realTimeStatDStream = calculateRealTimeStat(adRealTimeValueDStream)

    // 需求：实时统计每天每个城市top3的域名
    calculateCityTop3Url(spark, realTimeStatDStream)


    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * 攻击流量实时统计
    *
    * @param realTimeLogDStream
    * @return
    */
  def calculateRealTimeStat(realTimeLogDStream: DStream[(String)]): DStream[(String, Long)] = {
    val mappedDStream = realTimeLogDStream.map { case (log) =>
      val logSplited = log.split(",")

      val dangerLevel = logSplited(0).split(":")(1)
      val timestamp = logSplited(1).split(":")(1)
      val happenedTime = new Date("1530463264487".toLong)
      val datekey = DateUtils.formatDateKey(happenedTime)
      val sourceIp = logSplited(2).split(":")(1)
      val srcIpArea = logSplited(3).split(":")(1)
      val targetIp = logSplited(4).split(":")(1)
      val `type` = logSplited(5).split(":")(1)
      val websiteUrl = logSplited(6).split(":")(1)

      val key = datekey + "_" + srcIpArea + "_" + websiteUrl

      (key, 1L)
    }

    val aggregatedDStream = mappedDStream.updateStateByKey[Long] { (values: Seq[Long], old: Option[Long]) =>

      var trackCount = 0L

      if (old.isDefined) {
        trackCount = old.get
      }

      for (value <- values) {
        trackCount += value
      }

      Some(trackCount)
    }

    aggregatedDStream
  }

  /**
    * 业务功能三：计算每天各省份的top3域名
    * (yyyyMMdd_city_websiteurl,trackCount)
    *
    * @param realTimeStatDStream
    */
  def calculateCityTop3Url(spark: SparkSession, realTimeStatDStream: DStream[(String, Long)]) {

    // 每一个batch rdd，都代表了最新的全量的每天各城市各域名的攻击量
    val rowsDStream = realTimeStatDStream.transform { dailyClickCountByProvinceRDD =>

      // datekey + "_" + srcIpArea + "_" + websiteUrl,trackCount

      // 将dailyAdClickCountByProvinceRDD转换为DataFrame
      val rowsRDD = dailyClickCountByProvinceRDD.map { case (keyString, count) =>

        val keySplited = keyString.split("_")
        val datekey = keySplited(0)
        val srcIpArea = keySplited(1)
        val websiteUrl = keySplited(2)
        val trackCount = count

        val date = DateUtils.formatDate(DateUtils.parseDateKey(datekey))

        (date, srcIpArea, websiteUrl, trackCount)

      }

      import spark.implicits._
      val dailyTrackCountBySrcIpAreaDF = rowsRDD.toDF("date", "src_ip_area", "website_url", "track_count")

      // 将dailyAdClickCountByProvinceDF，注册成一张临时表
      dailyTrackCountBySrcIpAreaDF.createOrReplaceTempView("daily_track_count_src_ip_area")

      // 使用Spark SQL执行SQL语句，配合开窗函数，统计出各身份top3热门的广告
      val cityTop3AdDF = spark.sql(
        "SELECT "
          + "date,"
          + "src_ip_area,"
          + "website_url,"
          + "track_count "
          + "FROM ( "
          + "SELECT "
          + "date,"
          + "src_ip_area,"
          + "website_url,"
          + "track_count,"
          + "ROW_NUMBER() OVER(PARTITION BY src_ip_area ORDER BY track_count DESC) rank "
          + "FROM daily_track_count_src_ip_area "
          + ") t "
          + "WHERE rank<=3"
      )

      cityTop3AdDF.rdd
    }

    // 每次都是刷新出来各个省份最热门的top3广告，将其中的数据批量更新到MySQL中
    rowsDStream.foreachRDD { rdd =>
      rdd.foreachPartition { item =>
        item.foreach{rdd=>
          print("-------------------"+rdd.getString(0)+" "+rdd.getString(1))
        }
        // 插入数据库
//        val cityTop3s = ArrayBuffer[CityTop3]()
//
//        for (item <- items) {
//          val date = item.getString(0)
//          val srcIpArea = item.getString(1)
//          val websiteUrl = item.getString(2)
//          val trackCount = item.getLong(3)
//
//          cityTop3s += CityTop3(date, srcIpArea, websiteUrl, trackCount)
//        }
//        CityTop3DAO.updateBatch(cityTop3s.toArray)

      }
    }
  }


}
