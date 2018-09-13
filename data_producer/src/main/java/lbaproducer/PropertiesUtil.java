package lbaproducer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static String getPropertyParam(String key){
        Properties property = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("producer.properties"));
            property.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property.getProperty(key);
    }


}
