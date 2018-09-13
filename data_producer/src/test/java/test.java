import java.math.BigDecimal;
import java.util.Random;

public class test {

    private static String[] platfromarr = {"okex", "huobi", "bibox", "biance", "gate"};
    private static String[] transpairarr = {"lba_btc", "lba_eth", "lba_usdt", "lba_bix", "lba_okb"};
    private static String[] directionarr = {"buy", "sell"};

   static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        while (true){
            System.out.println(randomLonLat(0,9999,0));
            Thread.sleep(0);

        }

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

    public static String randomLonLat(double MinLon, double MaxLon,int i) {
        Random random = new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(i, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        return lon;
    }

}
