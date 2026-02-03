package com.parabank.pages;

import com.parabank.utils.ConfigReader;
import com.parabank.utils.WaitUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
    protected WebDriver driver;
    protected WaitUtil waitUtil;
    protected Logger logger;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver);
        this.logger = LogManager.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
    }

    protected void enterText(WebElement element, String text) {
        try {
            waitUtil.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text: " + text);
        } catch (Exception e) {
            logger.error("Failed to enter text: " + e.getMessage());
            throw e;
        }
    }

    protected void clickElement(WebElement element) {
        try {
            waitUtil.waitForElementToBeClickable(element);
            element.click();
            logger.info("Clicked on element");
        } catch (Exception e) {
            logger.error("Failed to click element: " + e.getMessage());
            throw e;
        }
    }

    protected String getElementText(WebElement element) {
        try {
            waitUtil.waitForElementToBeVisible(element);
            return element.getText();
        } catch (Exception e) {
            logger.error("Failed to get element text: " + e.getMessage());
            throw e;
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getAttributeValue(WebElement element, String attribute) {
        try {
            return element.getAttribute(attribute);
        } catch (Exception e) {
            logger.error("Failed to get attribute: " + e.getMessage());
            throw e;
        }
    }
}
