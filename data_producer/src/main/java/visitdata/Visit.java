package visitdata;

import java.io.Serializable;


/**
 * 数据模拟   bb交易信息
 */
public class Visit implements Serializable {

    //  3628296,0.1191,242.313,buy,2018-07-19 18:36:41,1531996601000,lba_usdt,okex
//  4384408,0.00024944,516.127,sell,2018-07-19 18:36:47,1531996607000,lba_eth,okex



    private Long userid;
    private String sessionid;
    private String accountVersion;
    private String platform;
    private String domain;      //域名
    private String page;
    private String referrer;
    private String language;
    private String screenSize;
    private String timestamp;
    private String sendTime;
    private String ip;
    private String userAgent;
    private String operatingSystem;
    private String clientVersion;
    private String deviceModel;
    private String latitude;    //精度
    private String longitude;   //维度
    private String vstRequestId;


    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(String accountVersion) {
        this.accountVersion = accountVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }


    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVstRequestId() {
        return vstRequestId;
    }

    public void setVstRequestId(String vstRequestId) {
        this.vstRequestId = vstRequestId;
    }


    @Override
    public String toString() {
        return
                userid +
                ",'" + sessionid +
                "," + accountVersion +
                "," + platform  +
                "," + domain +
                "," + page  +
                "," + referrer  +
                "," + language  +
                "," + screenSize  +
                "," + timestamp  +
                "," + sendTime  +
                "," + ip  +
                "," + userAgent  +
                "," + operatingSystem  +
                "," + clientVersion  +
                "," + deviceModel  +
                "," + latitude  +
                "," + longitude +
                "," + vstRequestId ;
    }
}
