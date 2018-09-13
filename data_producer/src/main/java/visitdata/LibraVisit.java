package visitdata;

import com.alibaba.fastjson.JSONObject;
import lbaproducer.LbaDeal;
import lbaproducer.PropertiesUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

//  3628296,0.1191,242.313,buy,2018-07-19 18:36:41,1531996601000,lba_usdt,okex
//  4384408,0.00024944,516.127,sell,2018-07-19 18:36:47,1531996607000,lba_eth,okex
public class LibraVisit {

   static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   static SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
    {
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
     }
        private static Random random = new Random();
        private static String[] domainarr = {"www.libracredit.io", "www.mycred.io"};
        private static String[] pagearr = {"/", "/loan#/", "/loan#/account", "/loan#/login","/loan#/acco", "/loan#/register","/loan#/assets/collateral","/loan#/assets","/loan#/assets/cash","/loan#/assets/orders/exchange","/loan#/order_list"};
        private static String[] refererarr = {"https://www.google.com/","https://libra.bounty.global/wallet/","http://libra.bounty.global/account/reputation","https://www.jinse.com/bitcoin/228274.html","https://libra.bounty.global/signup","https://libra.bounty.global/wallet/","https://www.google.com.ua/","https://www.google.ru/","https://www.google.de/","https://www.jinse.com/bitcoin/228309.html","http://libra.bounty.global/youtube","https://libra.bounty.global/","https://www.libracredit.io/loan/","https://www.bing.com/","android-app://org.telegram.messenger","http://libra.bounty.global/signup?ref=ncberjqzd","https://coinmarketcap.com/currencies/libra-credit/","https://coinmarketcap.com/currencies/libra-credit/","https://coinmarketcap.com/currencies/libra-credit/","https://labs.binance.com/","https://labs.binance.com/","https://libra.bounty.global/setting","https://www.google.com/","https://42fund.io/portfolio/","https://coinmarketcap.com/watchlist/","https://libra.bounty.global/","https://libra.bounty.global/setting","http://libra.bounty.global/youtube/apply","https://yandex.ru/","https://libra.bounty.global/invite/twitter","https://coinmarketcap.com/currencies/libra-credit/","https://www.libracredit.io/loan#/","android-app://org.telegram.messenger","https://www.google.com/","https://libra.bounty.global/setting","https://www.google.ca/","http://bounty.global/","https://labs.binance.com/","https://www.google.co.ve/","https://www.libracredit.io/loan/","https://www.bounty.global/","android-app://org.telegram.messenger","https://libra.bounty.global/invite/telegram","https://libra.bounty.global/youtube/apply","http://libra.bounty.global/account/linked","https://www.libracredit.io/loan/","https://coinmarketcap.com/currencies/libra-credit/"};
        private static String[] languagearr = {"id","ja","en","en","en","en","vi","en","ru","ru","ru","en","de","pl","tr","en","en","zh-hans","en","ko","en","c","en","ru","ru","en","de","en","en","id","en","en","ru","id","zh-hans","en","en","en","ru","fr","ru","ru","zh-hans","en","zh-hans","ru","uk","ru","de","zh-hans","ru","ru","de","en","en","en","ru","en","en","en","ru","ru","tr","ru","zh-hans","en","en","ru","ru","ru","es","en","ru","en","ru","zh-hans","zh-hans","zh-hans","zh-hans","en","en","zh-hans","en","zh-hans","zh-hans","ko","zh-hans","zh","zh-hans","zh-hans","pt","en","zh-hans","en","zh-hans","zh-hans","zh-hans","zh-hans","en","en","zh-hans","zh-hans","zh-hans","en","zh","zh-hans","zh-hans","zh-hans","zh-hans","en","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","c","en","zh-hans","zh-hans","en","zh-hans","zh-hans","en","zh-hans","en","zh-hans","en","en","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","en","zh-hans","en","en","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","en","ru","zh-hans","en","ru","zh-hans","en","zh-hans","zh-hans","en","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","ru","zh-hans","en","zh-hans","en","en","zh-hans","en","zh-hans","zh-hans","en","en","zh-hans","en","en","zh-hans","zh-hans","vi","ko","en","en","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","de","ru","ko","zh-hans","de","zh-hans","vi","zh-hans","zh-hans","vi","en","en","zh-hans","zh-hans","en","zh-hans","zh-hans","en","zh-hans","en","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","zh-hans","en","zh-hans","en","en","vi","en","pt","ru","en","zh-hans","en","ru","zh-hans","zh-hans","zh-hans","ru","ko","en","en","zh-hans","en","ko","id","zh-hans","en","c","en","zh-hans","en","zh-hans","en","th","zh-hans","en","en","en","de","zh-hans","zh-hans","ko","ko","sl","zh-hans","zh-hans","zh-hans","zh-hans","en","zh-hans","vi","zh-hans","zh-hans","en","ko","zh-hans","en","ru","zh-hans","zh-hans","en","zh-hans","en","en","tr","zh-hans","es","zh-hans","zh-hans","zh-hans","en","zh-hans","en","ko","zh-hans","zh-hans","en","en"};
        private static String[] screenSizearr = {"1600*900", "1920*1080","800*1280","1366*768","360*640","1440*900","210*568","1024*768","320*570","1536*864","800*600","1371*771","375*812","375*667","360*640"};
        private static String[] userAgentarr={"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3503.0 Safari/537.36","Mozilla/5.0 (Linux; Android 5.1; Flare_X_V2 Build/LMY47D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) coc_coc_browser/72.4.208 Chrome/66.4.3359.208 Safari/537.36","Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.103 YaBrowser/18.7.0.2695 Yowser/2.5 Safari/537.36","Mozilla/5.0 (Linux; Android 8.1.0; COL-L29 Build/HUAWEICOL-L29) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 YaBrowser/17.11.0.542.00 Mobile Safari/537.36","Mozilla/5.0 (Linux; Android 8.0.0; Lenovo K8 Plus Build/OMC27.70-39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Linux; Android 7.0; HUAWEI CAN-L11 Build/HUAWEICAN-L11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Linux; Android 5.1.1; A37fw Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0","Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.0 Safari/537.36","Mozilla/5.0 (Linux; Android 5.1; itel it1508 Build/LMY47D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.64","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.71 (Edition Yx)","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Linux; Android 7.0; G3312 Build/43.0.A.7.70) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Linux; Android 6.0.1; ASUS_Z00UD Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Linux; U; Android 7.1.2; id-id; Redmi 4X Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.128 Mobile Safari/537.36 XiaoMi/MiuiBrowser/9.8.3","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0","Mozilla/5.0 (Windows NT 6.3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3503.0 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.103 YaBrowser/18.7.0.2695 Yowser/2.5 Safari/537.36","Mozilla/5.0 (iPad; CPU OS 11_4_1 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) GSA/55.0.206190063 Mobile/15G77 Safari/604.1","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36 OPR/47.0.2631.71","Mozilla/5.0 (Linux; Android 5.1; Primo RX4 Build/LMY47I) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.103 YaBrowser/18.7.0.2695 Yowser/2.5 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0","Mozilla/5.0 (Linux; Android 8.1.0; Aquaris X Build/OPM1.171019.026) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36 QIHU 360SE","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.71 (Edition Yx)","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134","Mozilla/5.0 (Linux; Android 6.0; HTC One_M8 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.2 Safari/605.1.15","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.2 Safari/605.1.15","Mozilla/5.0 (Linux; Android 5.1.1; A37fw Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.103 YaBrowser/18.7.0.2695 Yowser/2.5 Safari/537.36","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Linux; Android 6.0; HTC One_M8 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:61.0) Gecko/20100101 Firefox/61.0","Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36","Mozilla/5.0 (Linux; Android 6.0.1; SM-J700F Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36","Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; CLT-AL00 Build/HUAWEICLT-AL00) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0   MQQBrowser/7.4 Mobile Safari/537.36"};
        private static String[] operatingSystemarr = {"Windows 7", "Mac OS X 10.11.6", "Mac OS X 10.12.6", "Windows 8.1", "Android 5.1", "Windows 8", "Windows 7", "Android 7.0", "Android 7.0", "Windows 10", "Android 8.1.0", "Android 8.0.0", "Mac OS X 10.13.6", "Android 7.0", "iOS 11.3", "Mac OS X 10.13.6", "Android 5.1.1", "Windows 7", "iOS 11.4.1", "Windows 7", "Android 5.1", "Windows 7", "Windows 10", "Windows 10", "Windows 7", "Windows 10", "Mac OS X 10.13.6", "Windows 10", "Android 7.0", "Android 6.0.1", "Windows 8.1", "Mac OS X 10.13.6", "Mac OS X 10.13.6", "Android 7.1.2", "Mac OS X 10.12.1", "Windows 8.1", "Windows 8.1", "Windows 10", "Windows 10", "iOS 11.4.1", "Windows 10", "Windows 8.1", "Windows 10", "Android 5.1", "Mac OS X 10.12.1", "Windows 7", "Windows 10", "Windows 7", "Android 8.1.0", "Windows 7", "Windows 10", "Windows 10", "Mac OS X 10.13.6", "iOS 11.4", "Windows 10", "Android 6.0", "Windows 7", "Windows 10", "iOS 11.4", "Windows 10", "Windows 10", "Windows 7", "iOS 11.4.1", "Windows 7", "Mac OS X 10.11.6", "Mac OS X 10.13.6", "Android 5.1.1", "Android 7.0", "Windows 7", "Windows 7", "Windows 8.1", "Windows 7", "Windows 10", "Android 6.0", "Windows 10", "iOS 11.4.1", "Windows 10", "Windows 7", "iOS 11.4.1", "Mac OS X 10.13", "Windows 8", "Mac OS X 10.13.6", "Android 6.0.1", "Android 8.1.0", "Windows 10", "Windows 10", "Android 7.1.1", "Mac OS X 10.13.6", "Windows 10", "Windows 10", "Windows 7", "Android 8.0.0", "Mac OS X 10.12.6", "Android 5.1.1", "Windows 7", "Windows 7", "Windows 7", "Windows 7", "Android 6.0", "Windows 10", "Mac OS X 10.11.6", "Windows 7", "Windows 7", "Android 7.0", "Windows 7", "Windows 10", "Android 7.0", "Windows 10", "Windows 10", "iOS 11.4.1", "Windows 10", "Windows 10", "Windows 7", "Mac OS X 10.12.5", "Windows 7", "Windows 10", "Windows 7", "Mac OS X 10.13.6", "Windows 7", "Windows 10", "Linux", "Windows 7", "Mac OS X 10.13.6", "Mac OS X 10.13.6", "iOS 11.4.1", "Mac OS X", "Windows 10", "Mac OS X 10.13.6", "Mac OS X 10.13.6", "Windows 7", "Windows 10", "Windows 7", "Mac OS X 10.13.4", "Mac OS X 10.12.6", "Windows 10", "Windows 10", "Windows 10", "Windows 7", "iOS 11.4.1", "iOS 11.4.1", "Mac OS X 10.13.6", "Windows 10", "Windows 10", "Windows 10", "Windows 7", "iOS 11.4.1", "Windows 10", "Mac OS X 10.13.4", "Windows 10", "Mac OS X 10.13.6", "Android 5.0.2", "Windows 7", "Windows 7", "Mac OS X 10.13.6", "Windows 7", "Windows 7", "Windows 10", "Windows 10", "Windows 7", "Windows 7", "Windows 10", "Windows 10", "Windows 7", "Android 7.1.2", "Windows 10", "Linux", "Mac OS X 10.13.0", "iOS 11.4.1", "Mac OS X 10.13.6", "Mac OS X 10.12.5", "Mac OS X 10.12.6", "Windows 7", "Windows 7", "Android 5.0.2", "Windows 10", "iOS 10.3", "Mac OS X 10.13.6", "Windows 10", "Windows 7", "Windows 7", "Windows 7", "Windows 7", "Windows 10", "Windows 10", "Windows 10", "Mac OS X 10.13.6", "Mac OS X 10.13.6", "Windows 7", "Windows 10", "Windows 10", "Windows 10", "Windows 10", "Windows 10", "Windows 10", "Windows 10", "Windows 10", "Windows 10", "Windows 7", "Windows 7", "Windows 7", "Windows 7", "Mac OS X 10.12.6", "Windows 7", "Mac OS X 10.13.6", "Windows 10", "Mac OS X 10.11.6", "Android 5.1.1", "Mac OS X 10.12.5", "Android 7.0", "Mac OS X 10.13.6", "Windows 10", "Windows 10", "Mac OS X 10.13.6", "iOS 10.3.2", "Windows 7", "iOS 11.4.1", "Mac OS X 10.13.6", "Android 8.1.0", "Windows 7", "Windows 7", "Mac OS X 10.13.2", "Android 8.1.0", "Windows 7", "Windows 10", "Mac OS X 10.13.3", "Windows 10", "Windows 10", "Windows 10", "Windows 7", "Mac OS X 10.12.4", "Windows 8", "iOS 11.4.1", "iOS 11.4.1", "Mac OS X 10.13.6", "Mac OS X 10.13.4", "Linux", "Windows 10", "Android 5.1.1", "Windows 10", "Android 8.1.0", "Windows 7", "Mac OS X 10.13.6", "Windows 10", "Linux", "Windows 7", "iOS 11.4.1", "Windows 7", "Mac OS X 10.13.6", "Mac OS X 10.11.6", "Windows 7", "Windows 7", "iOS 11.4", "Android 7.1.1", "Windows 7", "Windows 10", "Windows 10", "Windows Vista", "Android 8.0.0", "Windows 10", "Windows 10", "Windows 10", "Windows 7", "Android 8.1", "Windows 8", "Windows 10", "Mac OS X 10.12.4"};
        private static String [] deviceModelarr ={"Other","Other","Other","Other","Flare_X_V2","Other","Other","XiaoMi Redmi Note 4","XiaoMi Redmi Note 4","Other","COL-L29","Lenovo K8 Plus","Other","HUAWEI CAN-L11","iPhone","Other","A37fw","Other","iPhone","Other","itel it1508","Other","Other","Other","Other","Other","Other","Other","G3312","Asus Z00UD","Other","Other","Other","XiaoMi Redmi 4X","Other","Other","Other","Other","Other","iPad","Other","Other","Other","Walton Primo RX4","Other","Other","Other","Other","Aquaris X","Other","Other","Other","Other","iPhone","Other","HTC One M8","Other","Other","iPhone","Other","Other","Other","iPhone","Other","Other","Other","A37fw","XiaoMi Redmi Note 4","Other","Other","Other","Other","Other","HTC One M8","Other","iPhone","Other","Other","iPhone","Other","Other","Other","Samsung SM-J700F","CLT-AL00","Other","Other","Oppo R11s","Other","Other","Other","Other","MHA-L29","Other","Samsung SM-J200H","Other","Other","Other","Other","LT4501","Other","Other","Other","Other","Samsung SM-J730GM","Other","Other","BLN-AL20","Other","Other","iPhone","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","iPhone","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","iPhone","iPhone","Other","Other","Other","Other","Other","iPhone","Other","Other","Other","Other","XiaoMi Redmi Note 3","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","XiaoMi Redmi 5 Plus","Other","Other","Other","iPhone","Other","Other","Other","Other","Other","XiaoMi Redmi Note 3","Other","iPad Pro","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Other","Samsung SM-J200G","Other","XiaoMi Redmi Note 4","Other","Other","Other","Other","iPad","Other","iPhone","Moto X 2014","XiaoMi Redmi Note 5 Pro"};

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
            Visit visits = initData();
            String result = JSONObject.toJSONString(visits);
            System.out.println(result);

        }
    }





    private static Visit initData() {
        Visit visit = new Visit();

            Long timestamp = System.currentTimeMillis();

            Long userid = Long.parseLong(df1.format(new Date(timestamp))+randomnumber(0,99999,0));
            String sessionid = "0409f29d-2242-4c95-a7a9-929ea9e379db";
            String accountVersion = "2.1.14.2";
            String platform = "Web";
            String domain=domainarr[random.nextInt(domainarr.length)];
            String page = pagearr[random.nextInt(pagearr.length)];
            String referrer = refererarr[random.nextInt(refererarr.length)];
            String language = languagearr[random.nextInt(languagearr.length)];
            String screenSize = screenSizearr[random.nextInt(screenSizearr.length)];
            Long sendTime=timestamp +5;
            String ip =getRandomIp();
            String userAgent = userAgentarr[random.nextInt(userAgentarr.length)];
            String operatingSystem = operatingSystemarr[random.nextInt(operatingSystemarr.length)];
            String clientVersion=randomnumber(0,1.6,1);
            String deviceModel = deviceModelarr[random.nextInt(deviceModelarr.length)];
            String latitude = randomLonLat(0,180);   //精度
            String longitude=randomLonLat(0,90);   //维度
            String vstRequestId="String vstRequestId=392f59846fe04e83";

            visit.setUserid(userid);
            visit.setSessionid(sessionid);
            visit.setAccountVersion(accountVersion);
            visit.setPlatform(platform);
            visit.setDomain(domain);
            visit.setPage(page);
            visit.setReferrer(referrer);
            visit.setLanguage(language);
            visit.setScreenSize(screenSize);
            visit.setSendTime(sendTime.toString());
            visit.setIp(ip);
            visit.setUserAgent(userAgent);
            visit.setOperatingSystem(operatingSystem);
            visit.setClientVersion(clientVersion);
            visit.setDeviceModel(deviceModel);
            visit.setLatitude(latitude);
            visit.setLongitude(longitude);
            visit.setVstRequestId(vstRequestId);


        return visit;

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

    //随机获取ip
    public static String getRandomIp() {

        // ip范围
        int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
                { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
                { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
                { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
                { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
                { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
                { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
                { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
                { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
                { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
        };

        Random rdint = new Random();
        int index = rdint.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    /*
     * 将十进制转换成IP地址
     */
    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }
//随机产生经纬度
    /**
     * @Title: randomLonLat
     * @Description: 在矩形内随机生成经纬度
     * @param MinLon：最小经度  MaxLon： 最大经度   MinLat：最小纬度   MaxLat：最大纬度    type：设置返回经度还是纬度
     * @return
     * @throws
     */
    public static String randomLonLat(double MinLon, double MaxLon) {
        Random random = new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        return lon;
    }

    public static String randomnumber(double min, double max,int i) {
        BigDecimal db = new BigDecimal(Math.random() * (max - min) + min);
        String lon = db.setScale(i, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        return lon;
    }
}
