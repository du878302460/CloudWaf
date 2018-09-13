package cloudwaf

import commons.conf.ConfigurationManager
import commons.constant.Constants
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel

/**
  * @author XIAOYONG
  * @qq 1803887695
  * @Description: ${todo}
  * @date 2018/6/2210:20
  */
object AnalysisSQL {


  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()

    val spark = SparkSession.builder().master("local[*]").appName("analysisisql").getOrCreate()
    //读取hadoop或者mysql中的数据
    val cloudWafAction = getCloudWafAction(spark)
    cloudWafAction.persist(StorageLevel.MEMORY_AND_DISK_SER)
    cloudWafAction.toDF().createOrReplaceTempView("biz_waf_history")


    // 需求一：source_ip_area 攻击源top 10
    val top10Privice = spark.sql("SELECT src_ip_area,COUNT(*) source_ip_area_count FROM `biz_waf_history` GROUP BY src_ip_area ORDER BY source_ip_area_count DESC LIMIT 10")
    top10Privice.write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_TOP10PRIVICE))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()


    // 需求二：source_ip 攻击IP最多
    val sourceip2CountRDD = spark.sql("SELECT source_ip,COUNT(*) source_ip_count FROM `biz_waf_history` GROUP BY source_ip order by source_ip_count DESC LIMIT 1")
    sourceip2CountRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_MAXSOURCEIP))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()

    // 需求三：typex 攻击类型top 10
    val top10TypeX2CountRDD = spark.sql("SELECT TYPE,COUNT(*) type_count FROM `biz_waf_history` GROUP BY TYPE ORDER BY type_count DESC LIMIT 10")

    top10TypeX2CountRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_TOP10TYPEX))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()


    // 需求四：target_ip_city 不是杭州的ip占比
    val cityRadioRDD = spark.sql("SELECT target_ip_city,COUNT(*) AS target_ip_city_count,(SELECT COUNT(*) FROM `biz_waf_history`) AS target_count,(1-COUNT(*)/(SELECT COUNT(*) FROM `biz_waf_history`)) AS radio FROM `biz_waf_history` GROUP BY target_ip_city")

    cityRadioRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", Constants.MYSQL_CLOUD_DEFENSE_TEST_CITYRADIO)
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()

    val endTime = System.currentTimeMillis()
    println("需要花费的时间" + (endTime - startTime) / 1000)
  }

  def getCloudWafAction(spark: SparkSession) = {
    import spark.implicits._
    val cloudWafActionDF = spark.read.parquet(ConfigurationManager.config.getString(Constants.HDFS_URL))
    cloudWafActionDF.as[CloudDefenceWaf].filter {
      item =>
        if (!"".equals(item.source_ip_city) && item.source_ip_city != null)
          false
        else
          true
    }
  }
}
