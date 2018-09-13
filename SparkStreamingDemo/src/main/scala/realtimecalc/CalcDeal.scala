package realtimecalc

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object CalcDeal {
  val conf = new SparkConf().setAppName("calcdeal").setMaster("local[*]");
  val ssc = new StreamingContext(conf,Seconds(3))




}
