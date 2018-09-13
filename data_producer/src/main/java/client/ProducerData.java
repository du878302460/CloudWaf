package client;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Random;

/**
 * @author XIAOYONG
 * @qq 1803887695
 * @Description:
 * @date 17:24 2018/6/27
 */
public class ProducerData {


    private static String[] sourceIpCity = {"北京", "上海", "杭州", "深圳", "广州"};
    private static String[] dangerLevel = {"高危", "中危", "低危"};
    private static String[] type = {"CSRF", "跨站脚本", "SQL注入", "代码执行", "参数异常请求", "webshell", "远程文件包含", "敏感信息泄露"};
    private static String[] websiteUrl = {"http://yunwaf.caiyongjian.cn", "http://www.baidu.com", "http://www.sina.com", "http://www.csdn.com", "http://www.qq.com"};
    private static String[] targetIp = {"183.129.218.236", "183.129.118.236", "192.168.100.122", "183.129.218.246", "183.129.118.206", "192.168.100.152"};
    private static Random random = new Random();

    public static void main(String[] args) {
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号192.168.2.202:9092,192.168.2.203:9092,192.168.2.204:9092
        props.put("bootstrap.servers", "192.168.16.120:9092");
//        props.put("bootstrap.servers", "192.168.2.202:9092,192.168.2.203:9092,192.168.2.204:9092");
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
        gather(producer);
    }

    private static void gather(KafkaProducer<String, String> producer){
        for (int i = 0; i < 20000; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BizWafHistoryBean[] bizWafHistoryBeans = initData();
            String result = JSONObject.toJSONString(bizWafHistoryBeans);
            System.out.println(result.toString());

            producer.send(new ProducerRecord<String, String>("kafka_demo", Integer.toString(i), result), new Callback() {

                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {

                            if (metadata != null) {

                                System.out.println(metadata.partition() + "---" + metadata.offset());
                            }
                        }
                    }
            );
        }
    }





    private static BizWafHistoryBean[] initData() {
        BizWafHistoryBean[] bizWafHistoryBeans=new BizWafHistoryBean[10];
        for (int i=0;i<10;i++) {
            BizWafHistoryBean bizWafHistoryBean = new BizWafHistoryBean();
            String sourceIpArea = sourceIpCity[random.nextInt(sourceIpCity.length - 1)];
            String dangerlevel = dangerLevel[random.nextInt(dangerLevel.length - 1)];
            String types = type[random.nextInt(type.length - 1)];
            String websiteurl = websiteUrl[random.nextInt(websiteUrl.length - 1)];
            String targetip = targetIp[random.nextInt(targetIp.length - 1)];
            String sourceip = targetIp[random.nextInt(targetIp.length - 1)];
            bizWafHistoryBean.setHappened_time(System.currentTimeMillis()+"");
            bizWafHistoryBean.setTarget_ip(targetip);
            bizWafHistoryBean.setSource_ip(sourceip);
            bizWafHistoryBean.setType(types);
            bizWafHistoryBean.setSrc_ip_area(sourceIpArea);
            bizWafHistoryBean.setWebsite_url(websiteurl);
            bizWafHistoryBean.setDanger_level(dangerlevel);
            bizWafHistoryBeans[i]=bizWafHistoryBean;
        }
        return bizWafHistoryBeans;

    }


}
