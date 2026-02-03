package com.parabank.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtil {
    private static final Logger logger = LogManager.getLogger(WaitUtil.class);
    private WebDriverWait wait;

    public WaitUtil(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public WebElement waitForElementToBeVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible: " + e.getMessage());
            throw e;
        }
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable: " + e.getMessage());
            throw e;
        }
    }

    public boolean waitForElementToBeInvisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            logger.error("Element still visible: " + e.getMessage());
            throw e;
        }
    }

    public void waitForPageTitle(String title) {
        try {
            wait.until(ExpectedConditions.titleContains(title));
        } catch (Exception e) {
            logger.error("Title not found: " + e.getMessage());
            throw e;
        }
    }

    public void waitForUrlToContain(String urlFragment) {
        try {
            wait.until(ExpectedConditions.urlContains(urlFragment));
        } catch (Exception e) {
            logger.error("URL does not contain: " + urlFragment + " - " + e.getMessage());
            throw e;
        }
    }
}
