package cloudwaf


object utils {
  /**
    * maxSourceIp2CountRDD.toDF().write
      .format("jdbc")
      .option("url", ConfigurationManager.config.getString(Constants.JDBC_URL))
      .option("dbtable", ConfigurationManager.config.getString(Constants.MYSQL_CLOUD_DEFENSE_TEST_MAXSOURCEIP))
      .option("user", ConfigurationManager.config.getString(Constants.JDBC_USER))
      .option("password", ConfigurationManager.config.getString(Constants.JDBC_PASSWORD))
      .mode(SaveMode.Overwrite)
      .save()
    */
//  object DBUtils{
//    def writeToMysql(rdd:RDD[Object],jdbc_url:String,dbtable:String,username:String,password:String): Unit ={
//      rdd.toDF
//    }
//  }
}
