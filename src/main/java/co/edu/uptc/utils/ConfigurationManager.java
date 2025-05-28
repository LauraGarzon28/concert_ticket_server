package co.edu.uptc.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    private static final Properties PROPERTIES = new Properties();
    private static final String FILE_NAME = "./data/config.properties";
    private static final InputStream INPUT_STREAM  = ConfigurationManager.class.getResourceAsStream(FILE_NAME);

    public static void load() throws FileNotFoundException, IOException{
        PROPERTIES.load(INPUT_STREAM);
    }
    public static String getValue(String key){
        if (PROPERTIES.isEmpty()){
            try {
                load();
            } catch (IOException e) {
                return null;
            }
        }
        return PROPERTIES.getProperty(key);
    }
}
