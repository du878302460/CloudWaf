package client;

import java.io.Serializable;


/**
 * 数据模拟
 */
public class BizWafHistoryBean implements Serializable {
    /**
     * 用户访问表
     *
     * @param website_id 网站id
     * @param website_url 网站域名
     * @param target_ip 目的IP
     * @param target_ip_area 被攻击的服务器所属地
     * @param target_ip_country 被攻击的服务器所属国家
     * @param target_ip_city 被攻击的服务器所属城市
     * @param source_ip 攻击源IP
     * @param source_ip_city 攻击IP所属城市
     * @param danger_level 严重等级
     */
    private String website_url;
    private String target_ip;
    private String source_ip;
    private String src_ip_area;
    private String danger_level;
    private String type;
    private String happened_time;

    public String getHappened_time() {
        return happened_time;
    }

    public void setHappened_time(String happened_time) {
        this.happened_time = happened_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getTarget_ip() {
        return target_ip;
    }

    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }


    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }

    public String getSrc_ip_area() {
        return src_ip_area;
    }

    public void setSrc_ip_area(String src_ip_area) {
        this.src_ip_area = src_ip_area;
    }

    public String getDanger_level() {
        return danger_level;
    }

    public void setDanger_level(String danger_level) {
        this.danger_level = danger_level;
    }
}
