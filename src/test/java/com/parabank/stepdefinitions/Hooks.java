package com.parabank.stepdefinitions;

import com.parabank.pages.RegisterPage;
import com.parabank.utils.ConfigReader;
import com.parabank.utils.ExtentReportManager;
import com.parabank.utils.SoftAssertManager;
import com.parabank.utils.TestContext;
import com.parabank.utils.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Hooks class for setup and teardown operations.
 * Handles automatic user registration before login tests.
 */
public class Hooks {
    private WebDriver driver;
    private RegisterPage registerPage;
    private Logger logger;
    private TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * This hook runs before scenarios tagged with @RequiresRegistration.
     * It registers a new user with unique credentials and stores them in TestContext.
     */
    @Before("@RequiresRegistration")
    public void setUpWithRegistration(Scenario scenario) {
        logger = LogManager.getLogger(this.getClass());
        ExtentReportManager.createTest(scenario.getName(), "BDD Cucumber Test with Registration");
        
        // Initialize soft assert for the scenario
        SoftAssertManager.init();
        
        driver = WebDriverFactory.initializeDriver(ConfigReader.getBrowser());
        registerPage = new RegisterPage(driver, testContext);
        
        logger.info("Starting scenario with registration: " + scenario.getName());
        
        // Perform registration with unique data
        registerPage.performRegistrationWithUniqueData();
        boolean registrationSuccess = registerPage.isRegistrationSuccessful();
        
        // HARD ASSERT: Registration success is critical - if it fails, we cannot proceed
        Assert.assertTrue(registrationSuccess, "User registration should be successful");
        
        // Store credentials in TestContext for later use in login steps
        testContext.setUsername(registerPage.getGeneratedUsername());
        testContext.setPassword(registerPage.getGeneratedPassword());
        
        logger.info("Registration completed. Username: " + testContext.getUsername());
        
        // Logout to return to login page for subsequent login tests
        driver.get("https://parabank.parasoft.com/parabank/logout.htm");
        logger.info("Logged out to prepare for login test");
        
        // Wait for logout to complete and page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Cleanup after scenario - ALWAYS closes browser
     * Runs LAST (order = 1) to ensure all screenshots/reports are captured first
     */
    @After(order = 1)
    public void tearDown(Scenario scenario) {
        try {
            // Assert all soft assertions at the end
            SoftAssertManager.assertAll();
            
            if (scenario.isFailed()) {
                ExtentReportManager.getTest().fail("Scenario Failed: " + scenario.getName());
            } else {
                ExtentReportManager.getTest().pass("Scenario Passed: " + scenario.getName());
            }
        } finally {
            // ALWAYS close browser after test completes - THIS RUNS LAST
            try {
                WebDriverFactory.quitDriver();
                logger.info("Browser closed after scenario: " + scenario.getName());
            } catch (Exception e) {
                logger.error("Error closing browser: " + e.getMessage());
            }
            
            // Clear test context
            if (testContext != null) {
                testContext.clear();
            }
            
            SoftAssertManager.clear();
            logger.info("Completed scenario: " + scenario.getName());
        }
    }
}
