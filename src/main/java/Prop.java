import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class Prop {
    private static Prop instance;
    private Properties properties;

    private Prop() {
        properties = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(input);

            Enumeration<?> e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                System.out.println("Key : " + key + ", Value : " + value);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Prop getInstance() {
        if (instance == null) {
            instance = new Prop();
        }
        return instance;
    }

    static String getProp(String key){
        if (instance == null) {
            instance = new Prop();
        }
        return instance.properties.getProperty(key);
    }
}
