package com.parabank.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class WebDriverFactory {
    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static WebDriver initializeDriver(String browserName) {
        WebDriver webDriver = null;

        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    webDriver = new ChromeDriver(chromeOptions);
                    logger.info("Chrome browser initialized");
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--start-maximized");
                    webDriver = new FirefoxDriver(firefoxOptions);
                    logger.info("Firefox browser initialized");
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-notifications");
                    webDriver = new EdgeDriver(edgeOptions);
                    logger.info("Edge browser initialized");
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }

            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
            webDriver.manage().deleteAllCookies();

            setDriver(webDriver);
            return webDriver;

        } catch (Exception e) {
            logger.error("Failed to initialize browser: " + e.getMessage());
            throw new RuntimeException("Browser initialization failed: " + e.getMessage());
        }
    }

    public static void quitDriver() {
        WebDriver webDriver = getDriver();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
            logger.info("Browser closed successfully");
        }
    }
}
