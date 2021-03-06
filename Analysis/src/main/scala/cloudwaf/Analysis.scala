package cloudwaf

import commons.conf.ConfigurationManager
import commons.constant.Constants
import commons.utils.NumberUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object Analysis {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("analyse")
    val sparkContext = new SparkContext(sparkConf)
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //读取hadoop或者mysql中的数据
    val cloudWafAction = getCloudWafAction(sparkSession)

    cloudWafAction.cache()

    // 需求一：source_ip_area 攻击源top 10
    val provice2CountRDD = getProvice2CountRDD(cloudWafAction: RDD[(CloudDefenceWaf)]): RDD[(String, Long)]
    val top10Provice = provice2CountRDD.sortBy(_._2, false).take(10)
    import sparkSession.implicits._
    top10Provice.foreach(println(_))
    val top10Privice2 = top10Provice.map(item => Top10Area(item._1, item._2.toLong))
    val top10Privice3 = sparkContext.makeRDD(top10Privice2)
    top10Privice3.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_TOP10PRIVICE))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()


    //需求二：source_ip 攻击IP最多
    val sourceip2CountRDD = getSourceIp2CountRDD(cloudWafAction: RDD[(CloudDefenceWaf)]): RDD[(String, Long)]
    val maxSourceIp = sourceip2CountRDD.sortBy(_._2, false).take(1)
    val maxSourceIp2Count = maxSourceIp.map(item => MaxSourceIp(item._1, item._2))
    val maxSourceIp2CountRDD = sparkContext.makeRDD(maxSourceIp2Count)
    maxSourceIp2CountRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_MAXSOURCEIP))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()


    //需求三：typex 攻击类型top 10
    val type2CountRDD = getType2Count(cloudWafAction: RDD[(CloudDefenceWaf)]): RDD[(String, Long)]
    val top10TypeX = type2CountRDD
      .sortBy(_._2, false)
      .take(10)
    val top10TypeX2Count = top10TypeX.map(item => Top10TypeX(item._1, item._2))
    val top10TypeX2CountRDD = sparkContext.makeRDD(top10TypeX2Count)

    top10TypeX2CountRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable",ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_TOP10TYPEX) )
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()


    //需求四：danger_level 高危类型占比
    val dangerLevel2CountRDD = getDengerLevel2Count(cloudWafAction: RDD[(CloudDefenceWaf)]): RDD[(String, Long)]
    var low_level = sparkContext.longAccumulator
    var high_level = sparkContext.longAccumulator
    dangerLevel2CountRDD.foreach {
      case (level, count) =>
        if (level.equals("高危")) {
          high_level.add(count.toLong)
        } else if (level.equals("低危")) {
          low_level.add(count.toLong)
        }
    }
    val lowLevel = low_level.value
    val highLevel = high_level.value
    val sumLevel = lowLevel + highLevel
    val lowRevelRadio = NumberUtils.formatDouble(lowLevel * 1.0 / sumLevel, 2)
    val highRevelRadio = NumberUtils.formatDouble(highLevel * 1.0 / sumLevel, 2)
    val dengerLevelRadioRDD = sparkContext.makeRDD(Array(DengerLevelRadio(sumLevel, lowLevel, highLevel, lowRevelRadio, highRevelRadio)))

    dengerLevelRadioRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_DENGERLEVELRADIO))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()

    // 需求五：target_ip_city 不是杭州的ip占比
    val hangzhou2Count = getHangzhouSourceIpArea(cloudWafAction: RDD[(CloudDefenceWaf)]): RDD[(String, Long)]
    val sourceIpArea2Count = cloudWafAction.count()
    val hangzhouAcculator = sparkContext.longAccumulator
    hangzhou2Count.foreach {
      case (city, count) =>
        hangzhouAcculator.add(count)
    }
    val otherCityCount = sourceIpArea2Count - hangzhouAcculator.value
    val hangzhouRadio = hangzhouAcculator.value * 1.0 / sourceIpArea2Count
    val otherCityRadio = 1 - hangzhouRadio
    val cityRadio = new HangzhouRadio(sourceIpArea2Count, hangzhouAcculator.value, otherCityCount, hangzhouRadio, otherCityRadio)
    val cityRadioRDD = sparkContext.makeRDD(Array(cityRadio))

    cityRadioRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_CITYRADIO))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()
  }

  def getCloudWafAction(sparkSession: SparkSession):RDD[CloudDefenceWaf] = {
    import sparkSession.implicits._
        val cloudWafActionDF = sparkSession.read.parquet(ConfigurationManager.config.getString(Constants.HDFS_URL))
    cloudWafActionDF.as[CloudDefenceWaf].rdd.filter {
      item =>
        if (!"".equals(item.source_ip_city) && item.source_ip_city != null)
          true
        else
          false
    }

  }

  def getProvice2CountRDD(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.target_ip_city, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }

  def getSourceIp2CountRDD(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.source_ip, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }

  def getType2Count(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.target_ip_city, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }

  def getDengerLevel2Count(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction.map(cloudWafActionItem => (cloudWafActionItem.danger_level, 1L))
    cloudWafto1.reduceByKey(_ + _)
  }

  def getHangzhouSourceIpArea(cloudWafAction: RDD[CloudDefenceWaf]): RDD[(String, Long)] = {
    val cloudWafto1 = cloudWafAction
      .filter { case cloudWafActionItem => {
        if (cloudWafActionItem.target_ip_city.equals("杭州市")) {
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
