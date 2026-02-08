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
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
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

    /**
     * Initialize WebDriver - supports both Local and Grid modes
     * 
     * @param browserName browser name (chrome, firefox, edge)
     * @return WebDriver instance
     */
    public static WebDriver initializeDriver(String browserName) {
        // Check if Grid mode is enabled
        if (ConfigReader.isGridModeEnabled()) {
            return initializeRemoteDriver(browserName);
        } else {
            return initializeLocalDriver(browserName);
        }
    }

    /**
     * Initialize Local WebDriver
     */
    private static WebDriver initializeLocalDriver(String browserName) {
        WebDriver webDriver = null;

        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    webDriver = new ChromeDriver(chromeOptions);
                    logger.info("Chrome browser initialized (Local Mode)");
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--start-maximized");
                    webDriver = new FirefoxDriver(firefoxOptions);
                    logger.info("Firefox browser initialized (Local Mode)");
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-notifications");
                    webDriver = new EdgeDriver(edgeOptions);
                    logger.info("Edge browser initialized (Local Mode)");
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

    /**
     * Initialize Remote WebDriver for Selenium Grid
     */
    private static WebDriver initializeRemoteDriver(String browserName) {
        WebDriver webDriver = null;
        String gridUrl = ConfigReader.getGridUrl();
        
        try {
            URL remoteAddress = new URL(gridUrl);
            logger.info("Connecting to Selenium Grid at: " + gridUrl);

            switch (browserName.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    webDriver = new RemoteWebDriver(remoteAddress, chromeOptions);
                    logger.info("Chrome browser initialized (Grid Mode)");
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--start-maximized");
                    webDriver = new RemoteWebDriver(remoteAddress, firefoxOptions);
                    logger.info("Firefox browser initialized (Grid Mode)");
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-notifications");
                    webDriver = new RemoteWebDriver(remoteAddress, edgeOptions);
                    logger.info("Edge browser initialized (Grid Mode)");
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser for Grid: " + browserName);
            }

            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
            webDriver.manage().deleteAllCookies();

            setDriver(webDriver);
            logger.info("Remote WebDriver created successfully on Grid");
            return webDriver;

        } catch (MalformedURLException e) {
            logger.error("Invalid Grid URL: " + gridUrl);
            throw new RuntimeException("Invalid Selenium Grid URL: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to connect to Selenium Grid: " + e.getMessage());
            throw new RuntimeException("Grid connection failed: " + e.getMessage());
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
