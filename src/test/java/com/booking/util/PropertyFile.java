package com.booking.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {

    private static FileInputStream input;
    private static Properties property = null;
    private static final Logger LOG = LogManager.getLogger(PropertyFile.class);

    public static String getProperty(final String key) {

        try {
            input = new FileInputStream("src/test/resources/application.properties");
            property = new Properties();
            property.load(input);
        } catch (FileNotFoundException ex) {
            LOG.error("Properties File Not Found", ex);
        } catch (IOException e) {
            LOG.error("IO Exception while loading Properties File", e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                LOG.error("IO Exception while closing file input stream", e);
            }
        }
        return property.getProperty(key).trim();
    }
}
