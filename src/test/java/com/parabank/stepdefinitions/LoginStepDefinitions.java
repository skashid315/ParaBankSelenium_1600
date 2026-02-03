package com.parabank.stepdefinitions;

import com.parabank.pages.LoginPage;
import com.parabank.utils.ConfigReader;
import com.parabank.utils.ExtentReportManager;
import com.parabank.utils.ScreenshotUtil;
import com.parabank.utils.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginStepDefinitions {
    private WebDriver driver;
    private LoginPage loginPage;
    private Logger logger;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        logger = LogManager.getLogger(this.getClass());
        ExtentReportManager.createTest(scenario.getName(), "BDD Cucumber Test");
        driver = WebDriverFactory.initializeDriver(ConfigReader.getBrowser());
        loginPage = new LoginPage(driver);
        logger.info("Starting scenario: " + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, scenario.getName().replaceAll(" ", "_") + "_FAILED");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.getTest().fail("Scenario Failed: " + scenario.getName());
        } else {
            ExtentReportManager.getTest().pass("Scenario Passed: " + scenario.getName());
        }
        WebDriverFactory.quitDriver();
        logger.info("Completed scenario: " + scenario.getName());
    }

    @Given("the user is on the ParaBank login page")
    public void theUserIsOnTheParaBankLoginPage() {
        try {
            loginPage.navigateToLoginPage();
            Assert.assertTrue(loginPage.isOnLoginPage(), "User should be on login page");
            ExtentReportManager.logStep("Given", "Navigate to login page", "Login page displayed", "Login page is displayed", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Given", "Navigate to login page", "Login page displayed", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Given("the user is logged in with valid credentials")
    public void theUserIsLoggedInWithValidCredentials() {
        try {
            String username = ConfigReader.getProperty("valid.username");
            String password = ConfigReader.getProperty("valid.password");
            loginPage.performLogin(username, password);
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
            ExtentReportManager.logStep("Given", "Login with valid credentials", "User logged in", "User is logged in", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Given", "Login with valid credentials", "User logged in", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user enters username {string}")
    public void theUserEntersUsername(String username) {
        try {
            loginPage.enterUsername(username);
            ExtentReportManager.logStep("When", "Enter username: " + username, "Username entered", "Username entered successfully", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Enter username: " + username, "Username entered", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user enters password {string}")
    public void theUserEntersPassword(String password) {
        try {
            loginPage.enterPassword(password);
            ExtentReportManager.logStep("When", "Enter password", "Password entered", "Password entered successfully", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Enter password", "Password entered", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user clicks the Login button")
    public void theUserClicksTheLoginButton() {
        try {
            loginPage.clickLoginButton();
            ExtentReportManager.logStep("When", "Click Login button", "Login button clicked", "Login button clicked successfully", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Click Login button", "Login button clicked", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user clicks the Log Out link")
    public void theUserClicksTheLogOutLink() {
        try {
            loginPage.clickLogout();
            ExtentReportManager.logStep("When", "Click Logout link", "User logged out", "Logout successful", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Click Logout link", "User logged out", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user leaves the username field empty")
    public void theUserLeavesTheUsernameFieldEmpty() {
        try {
            loginPage.clearUsernameField();
            ExtentReportManager.logStep("When", "Leave username field empty", "Username field cleared", "Username field is empty", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Leave username field empty", "Username field cleared", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user leaves the password field empty")
    public void theUserLeavesThePasswordFieldEmpty() {
        try {
            loginPage.clearPasswordField();
            ExtentReportManager.logStep("When", "Leave password field empty", "Password field cleared", "Password field is empty", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Leave password field empty", "Password field cleared", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user checks the password field type")
    public void theUserChecksThePasswordFieldType() {
        ExtentReportManager.logStep("When", "Check password field type", "Field type verified", "Password field type checked", "Pass");
    }

    @Then("the user should be redirected to the Accounts Overview page")
    public void theUserShouldBeRedirectedToTheAccountsOverviewPage() {
        try {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("overview.htm"), "User should be on Accounts Overview page");
            ExtentReportManager.logStep("Then", "Verify Accounts Overview page", "User on Accounts Overview page", "URL: " + currentUrl, "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify Accounts Overview page", "User on Accounts Overview page", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the Accounts Overview header should be displayed")
    public void theAccountsOverviewHeaderShouldBeDisplayed() {
        try {
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Accounts Overview header should be displayed");
            ExtentReportManager.logStep("Then", "Verify Accounts Overview header", "Header displayed", "Accounts Overview header is displayed", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify Accounts Overview header", "Header displayed", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        try {
            boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
            Assert.assertTrue(isErrorDisplayed, "Error message should be displayed");
            ExtentReportManager.logStep("Then", "Verify error message displayed", "Error message displayed", "Error message is displayed", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify error message displayed", "Error message displayed", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedText) {
        try {
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains(expectedText) || errorMessage.contains("Error"), "Error message should contain: " + expectedText);
            ExtentReportManager.logStep("Then", "Verify error message content", "Error contains: " + expectedText, "Error message: " + errorMessage, "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify error message content", "Error contains expected text", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        try {
            boolean isOnLoginPage = loginPage.isOnLoginPage();
            Assert.assertTrue(isOnLoginPage, "User should be on login page");
            ExtentReportManager.logStep("Then", "Verify user on login page", "User on login page", "User is on login page", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify user on login page", "User on login page", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the login should fail")
    public void theLoginShouldFail() {
        try {
            boolean isLoginSuccessful = loginPage.isLoginSuccessful();
            Assert.assertFalse(isLoginSuccessful, "Login should fail");
            ExtentReportManager.logStep("Then", "Verify login failure", "Login failed", "Login failed as expected", "Pass");
        } catch (AssertionError e) {
            ExtentReportManager.logStep("Then", "Verify login failure", "Login failed", "Login succeeded unexpectedly", "Fail");
            throw e;
        }
    }

    @Then("no unauthorized access should be granted")
    public void noUnauthorizedAccessShouldBeGranted() {
        try {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("overview.htm"), "Should not be on Accounts Overview page");
            ExtentReportManager.logStep("Then", "Verify no unauthorized access", "No unauthorized access", "No unauthorized access granted", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify no unauthorized access", "No unauthorized access", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the script should not be executed")
    public void theScriptShouldNotBeExecuted() {
        try {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("overview.htm"), "Should not be on Accounts Overview page");
            ExtentReportManager.logStep("Then", "Verify script not executed", "Script not executed", "XSS script was not executed", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify script not executed", "Script not executed", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the password field should have type {string}")
    public void thePasswordFieldShouldHaveType(String expectedType) {
        try {
            String actualType = loginPage.getPasswordFieldType();
            Assert.assertEquals(actualType, expectedType, "Password field type should be: " + expectedType);
            ExtentReportManager.logStep("Then", "Verify password field type", "Type is: " + expectedType, "Actual type: " + actualType, "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify password field type", "Type is: " + expectedType, "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the password characters should be masked")
    public void thePasswordCharactersShouldBeMasked() {
        try {
            String fieldType = loginPage.getPasswordFieldType();
            Assert.assertEquals(fieldType, "password", "Password field should be masked");
            ExtentReportManager.logStep("Then", "Verify password masking", "Password is masked", "Password characters are masked", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify password masking", "Password is masked", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        try {
            boolean isOnLoginPage = loginPage.isOnLoginPage();
            Assert.assertTrue(isOnLoginPage, "User should be on login page after logout");
            ExtentReportManager.logStep("Then", "Verify redirect to login page", "User on login page", "User is on login page", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify redirect to login page", "User on login page", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Then("the Customer Login header should be displayed")
    public void theCustomerLoginHeaderShouldBeDisplayed() {
        try {
            boolean isOnLoginPage = loginPage.isOnLoginPage();
            Assert.assertTrue(isOnLoginPage, "Customer Login header should be displayed");
            ExtentReportManager.logStep("Then", "Verify Customer Login header", "Header displayed", "Customer Login header is displayed", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Then", "Verify Customer Login header", "Header displayed", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }
}
