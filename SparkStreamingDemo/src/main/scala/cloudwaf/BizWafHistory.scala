package cloudwaf

/**
  * @author XIAOYONG
  * @qq 1803887695
  * @Description:
  * @date 16:15 2018/6/29
  */

case class BizWafHistory(
                          website_url:String,
                          target_ip:String,
                          source_ip:String,
                          src_ip_area:String,
                          danger_level:String,
                          `type`:String,
                          happened_time:String
                        )
