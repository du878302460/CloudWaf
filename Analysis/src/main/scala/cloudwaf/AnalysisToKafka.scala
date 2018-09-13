package cloudwaf

import java.util.Properties

import commons.conf.ConfigurationManager
import commons.constant.Constants
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author XIAOYONG
  * @qq 1803887695
  * @Description: ${todo}
  * @date 2018/6/130:58
  */
object AnalysisToKafka {

  def main(args: Array[String]): Unit = {

    val startTime = System.currentTimeMillis()

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("analyse_to_kafka")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sparkContext = new SparkContext(sparkConf)

    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()


    //读取hadoop或者mysql中的数据
    val cloudWafAction = getCloudWafAction(sparkSession)
    cloudWafAction.take(10).foreach(println(_))
    cloudWafAction.cache()
    //
    //    // 需求一：source_ip_area 攻击源top 10
    var producer: KafkaProducer[String, String] = null
    val props = new Properties()
    props.put("bootstrap.servers", ConfigurationManager.config.getString(Constants.KAFKA_TOPICS))
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[StringSerializer].getName)

    val provice2CountRDD = getProvice2CountRDD(cloudWafAction: RDD[(CloudDefenceWaf)]): RDD[(String, Long)]
    val top10Provice = provice2CountRDD.sortBy(_._2, false).take(10)
    val top10Privice2 = top10Provice.map(item => Top10Area(item._1, item._2.toLong))
    val top10Privice3 = sparkContext.makeRDD(top10Privice2)
    top10Privice3.foreach(println(_))
    producer = new KafkaProducer[String, String](props)
    top10Privice3.foreachPartition(partition => {
      partition.foreach {
        case item => {
          producer.send(new ProducerRecord[String, String]("top10Privice", item.source_ip_area, item.count.toString))
        }
      }
    })
    producer.close()
  }

  def getCloudWafAction(sparkSession: SparkSession) = {
    import sparkSession.implicits._
    val cloudWafActionDF = sparkSession.read.parquet(Constants.HDFS_URL)
    cloudWafActionDF.as[CloudDefenceWaf].rdd.filter {
      item =>
        if (!"".equals(item.source_ip_city) && item.source_ip_city != null)
          true
        else
          false
    }

  }

  def getProvice2CountRDD(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.source_ip_city, 1L))

    cloudWafto1.reduceByKey(_ + _)
  }

  def getSourceIp2CountRDD(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.source_ip, 1L))
    cloudWafto1.reduceByKey(_ + _)

  }

  def getType2Count(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.source_ip_city, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }

  def getDengerLevel2Count(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(_root_.scala.Predef.String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.danger_level, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }

  def getHangzhouSourceIpArea(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction
      .filter { case cloudWafActionItem => {
        if ("杭州市".equals(cloudWafActionItem.target_ip_city)) {
          true
        } else {
          false
        }
      }

      }
      .map(cloudWafActionItem => (cloudWafActionItem.target_ip_city, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }


}
