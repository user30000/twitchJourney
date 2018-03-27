package util;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class Prop {
    private static Prop instance;
    private Properties properties;

    private Prop() {
        properties = new Properties();
        try {
            URL u = getClass().getResource("/config.properties");
            InputStream input;
            if (u != null) {
                input = getClass().getClassLoader().getResourceAsStream("config.properties");
            } else {
                input = getClass().getClassLoader().getResourceAsStream("public.properties");
            }

            properties.load(input);

            Enumeration<?> e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
            }
        } catch (Exception e) {
            System.out.println("Не обнаружен файл config.properties");
        }
    }

    public static String getProp(String key) {
        if (instance == null) {
            instance = new Prop();
        }
        return instance.properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(getProp(key));
    }
}
