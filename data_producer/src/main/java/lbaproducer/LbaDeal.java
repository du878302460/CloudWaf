package lbaproducer;

import java.io.Serializable;


/**
 * 数据模拟   bb交易信息
 */
public class LbaDeal implements Serializable {

    //  3628296,0.1191,242.313,buy,2018-07-19 18:36:41,1531996601000,lba_usdt,okex
//  4384408,0.00024944,516.127,sell,2018-07-19 18:36:47,1531996607000,lba_eth,okex


    private String orderid;
    private String price;
    private String amount;
    private String direction;
    private String date;
    private String timestamp;
    private String transpair;
    private String platfrom;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTranspair() {
        return transpair;
    }

    public void setTranspair(String transpair) {
        this.transpair = transpair;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }


    public String toString() {
        return this.orderid+","+this.price+","+amount+","+direction+","+date+","+timestamp+","+transpair+","+platfrom;
    }
}
