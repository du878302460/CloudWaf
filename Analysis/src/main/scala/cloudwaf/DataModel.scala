package cloudwaf


//***************** 输入表 *********************

/**
  *用户访问表
  *
  * @param id
  * @param website_id 网站id
  * @param website_url 网站域名
  * @param target_ip 目的IP
  * @param target_ip_area 被攻击的服务器所属地
  * @param target_ip_country 被攻击的服务器所属国家
  * @param target_ip_city 被攻击的服务器所属城市
  * @param source_ip 攻击源IP
  * @param src_ip_area 攻击源归属地
  * @param source_ip_country 攻击IP所属国家
  * @param source_ip_city 攻击IP所属城市
  * @param danger_level 严重等级
  */
case class CloudDefenceWaf(
                            id:String,
                            website_id:String,
                            website_url:String,
                            target_ip:String,
                            target_ip_area:String,
                            target_ip_country:String,
                            target_ip_city:String,
                            source_ip:String,
                            src_ip_area:String,
                            source_ip_country:String,
                            source_ip_city:String,
                            danger_level:String
                          )


//***************** 输出表 *********************

/**
  * source_ip_area 攻击源top 10
  * @param source_ip_area 攻击源
  * @param count 攻击的数量
  */
case class Top10Area(source_ip_area:String,
                     count:Long)

/**source_ip 攻击IP最多
  *
  * @param source_ip 攻击IP
  * @param count  攻击的数量
  */
case class MaxSourceIp(source_ip:String,count:Long)

/**
  * typex 攻击类型top 10
  * @param typex 攻击类型
  * @param count  攻击的数量
  */
case class Top10TypeX(typex:String,count:Long)

/**
  * danger_level 高危类型占比
  * @param level_count 高危类型数量
  * @param low_level_count 低危类型数量
  * @param high_level_count 高危类型数量
  * @param low_level_radio 低微类型占比
  * @param high_level_radio 高危类型占比
  */
case class DengerLevelRadio(level_count:Long,
                            low_level_count:Long,
                            high_level_count:Long,
                            low_level_radio:Double,
                            high_level_radio:Double)

/**
  * target_ip_city 不是杭州的ip占比
  * @param target_id_count 目标城市的数量
  * @param hangzhou_target_id 杭州城市的id
  * @param othercity_target_id 其他城市的id
  * @param hangzhou_radio 杭州城市的所占比例
  * @param othercity_Radio 其他城市的所占比例
  */
case class HangzhouRadio(target_id_count:Long,hangzhou_target_id:Long,othercity_target_id:Long,hangzhou_radio: Double,othercity_Radio:Double)


