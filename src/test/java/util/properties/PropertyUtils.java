package util.properties;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class that provides utilities for properties.
 */
@Slf4j
public class PropertyUtils {

    public static Properties readProperty(String property, Properties prop, Class clazz) {
        if (prop == null) {
            prop = new Properties();
        }
        InputStream stream = clazz.getClassLoader().getResourceAsStream(property);
        try {
            prop.load(stream);
        } catch (IOException e) {
            log.error("ERROR: " + e.getMessage());
        }
        return prop;
    }

}
