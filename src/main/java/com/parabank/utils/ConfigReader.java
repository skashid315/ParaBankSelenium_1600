package com.parabank.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE = "src/test/resources/config/config.properties";

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            logger.info("Configuration loaded successfully from: " + CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration file: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
        }
        return value;
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }

    /**
     * Check if Selenium Grid mode is enabled
     * @return true if grid.enabled=true in config
     */
    public static boolean isGridModeEnabled() {
        String gridEnabled = getProperty("grid.enabled");
        return "true".equalsIgnoreCase(gridEnabled);
    }

    /**
     * Get Selenium Grid Hub URL
     * @return Grid URL (default: http://localhost:4444/wd/hub)
     */
    public static String getGridUrl() {
        String gridUrl = getProperty("grid.url");
        return gridUrl != null ? gridUrl : "http://localhost:4444/wd/hub";
    }
}
