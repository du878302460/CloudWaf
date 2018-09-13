package cloudwaf

import commons.pool.CreateMySqlPool

import scala.collection.mutable.ArrayBuffer

/**
  * 各省份top3热门广告DAO实现类
  *
  * @author Administrator
  *
  */
object CityTop3DAO {

  def updateBatch(cityTop3s: Array[CityTop3]) {
    // 获取对象池单例对象
    val mySqlPool = CreateMySqlPool()
    // 从对象池中提取对象
    val client = mySqlPool.borrowObject()

    // dateProvinces可以实现一次去重
    // AdProvinceTop3：date province adid clickCount，由于每条数据由date province adid组成
    // 当只取date province时，一定会有重复的情况
    val dateCitys = ArrayBuffer[String]()

    for (cityTop3 <- cityTop3s) {
      // 组合新key
      val key = cityTop3.date + "_" + cityTop3.src_ip_Area

      // dateProvinces中不包含当前key才添加
      // 借此去重
      if (!dateCitys.contains(key)) {
        dateCitys += key
      }
    }

    // 根据去重后的date和province，进行批量删除操作
    // 先将原来的数据全部删除
    val deleteSQL = "DELETE FROM city_top3 WHERE date=? AND city=?"

    val deleteParamsList: ArrayBuffer[Array[Any]] = ArrayBuffer[Array[Any]]()

    for (dateCity <- dateCitys) {

      val dateProvinceSplited = dateCity.split("_")
      val date = dateProvinceSplited(0)
      val city = dateProvinceSplited(1)

      val params = Array[Any](date, city)
      deleteParamsList += params
    }

    client.executeBatch(deleteSQL, deleteParamsList.toArray)

    // 批量插入传入进来的所有数据
    val insertSQL = "INSERT INTO city_top3 VALUES(?,?,?,?)"

    val insertParamsList: ArrayBuffer[Array[Any]] = ArrayBuffer[Array[Any]]()

    // 将传入的数据转化为参数列表
    for (cityTop3 <- cityTop3s) {
      insertParamsList += Array[Any](cityTop3.date, cityTop3.src_ip_Area, cityTop3.track_count)
    }

    client.executeBatch(insertSQL, insertParamsList.toArray)

    // 使用完成后将对象返回给对象池
    mySqlPool.returnObject(client)
  }

}
