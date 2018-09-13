package lbaproducer;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

//  3628296,0.1191,242.313,buy,2018-07-19 18:36:41,1531996601000,lba_usdt,okex
//  4384408,0.00024944,516.127,sell,2018-07-19 18:36:47,1531996607000,lba_eth,okex
public class LbatoKafka {

   static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   static SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
    {
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
     }
        private static String[] platfromarr = {"okex", "huobi", "bibox", "biance", "gate"};
        private static String[] transpairarr = {"lba_btc", "lba_eth", "lba_usdt", "lba_bix", "lba_okb"};
        private static String[] directionarr = {"buy", "sell"};
        private static Random random = new Random();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", PropertiesUtil.getPropertyParam("bootstrap.servers"));
        // 等待所有副本节点的应答
        props.put("acks", "all");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 16384);
        // 请求延时
        props.put("linger.ms", 1);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);
        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        long sleeptime = Long.parseLong(PropertiesUtil.getPropertyParam("sleeptime"));
        long dataamount = Long.parseLong(PropertiesUtil.getPropertyParam("dataamount"));
        String topics = PropertiesUtil.getPropertyParam("kafka.topics");
        gather(producer,sleeptime,dataamount,topics);
    }

    private static void gather(KafkaProducer<String, String> producer, long sleeptime, long dataamount,String topics){
        for (long i = 0; i < dataamount; i++) {
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LbaDeal[] lbaDeals = initData();

            for (LbaDeal lbaDeal : lbaDeals) {
                producer.send(new ProducerRecord<String, String>(topics, lbaDeal.toString()));
//                System.out.println(lbaDeal.toString());
            }
        }
    }





    private static LbaDeal[] initData() {
        LbaDeal[] lbaDeals =new LbaDeal[5];
        for (int i=0;i<5;i++) {
            Long timestamp = System.currentTimeMillis();
            LbaDeal lbaDeal = new LbaDeal();
            String transpair = transpairarr[random.nextInt(transpairarr.length )];
            String platfrom = platfromarr[random.nextInt(platfromarr.length )];
            String direction = directionarr[random.nextInt(directionarr.length)];
            Long orderid = Long.parseLong(df1.format(new Date(timestamp))+randomnumber(0,99999,0));
            Double price = Double.valueOf(randomnumber(0,1.2,4));
            Double amount = Double.valueOf(randomnumber(0,1000000,4));



            lbaDeal.setOrderid(orderid.toString());
            lbaDeal.setPrice(price.toString());
            lbaDeal.setAmount(amount.toString());
            lbaDeal.setDirection(direction);
            lbaDeal.setDate(df.format(new Date(timestamp)));
            lbaDeal.setTimestamp(timestamp.toString());
            lbaDeal.setTranspair(transpair);
            lbaDeal.setPlatfrom(platfrom);
            lbaDeals[i]= lbaDeal;
        }
        return lbaDeals;

    }

    private static String getnumber(int i , int j) {
        // 获得随机数
        Random random = new Random();
        double pross = (1 + random.nextDouble()) * Math.pow(10, i);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        int last = i+2+j;
        // 返回固定的长度的随机数
        if (j==0){
            return fixLenthString.substring(1,i+1);
        }
        return fixLenthString.substring(1,last);
    }
    public static String randomnumber(double min, double max,int i) {
        BigDecimal db = new BigDecimal(Math.random() * (max - min) + min);
        String lon = db.setScale(i, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        return lon;
    }

}
