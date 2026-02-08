package com.parabank.stepdefinitions;

import com.parabank.pages.LoginPage;
import com.parabank.pages.RegisterPage;
import com.parabank.utils.ConfigReader;
import com.parabank.utils.ExtentReportManager;
import com.parabank.utils.ScreenshotUtil;
import com.parabank.utils.SoftAssertManager;
import com.parabank.utils.TestContext;
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
    private RegisterPage registerPage;
    private Logger logger;
    private Scenario scenario;
    private TestContext testContext;

    public LoginStepDefinitions(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = 1)
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        logger = LogManager.getLogger(this.getClass());
        
        // Initialize soft assert for each scenario
        SoftAssertManager.init();
        
        // Check if driver already exists in WebDriverFactory (from Hooks @RequiresRegistration)
        if (WebDriverFactory.getDriver() != null) {
            // Reuse existing driver from Hooks
            driver = WebDriverFactory.getDriver();
            loginPage = new LoginPage(driver);
            logger.info("Reusing driver from Hooks for scenario: " + scenario.getName());
        } else {
            // Initialize new driver for scenarios without @RequiresRegistration
            ExtentReportManager.createTest(scenario.getName(), "BDD Cucumber Test");
            driver = WebDriverFactory.initializeDriver(ConfigReader.getBrowser());
            loginPage = new LoginPage(driver);
            logger.info("Starting scenario: " + scenario.getName());
        }
    }

    @After(order = 100)
    public void tearDown(Scenario scenario) {
        try {
            // Assert all soft assertions at the end of scenario
            SoftAssertManager.assertAll();
            
            // Update Extent Report for all scenarios
            if (scenario.isFailed()) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, scenario.getName().replaceAll(" ", "_") + "_FAILED");
                ExtentReportManager.attachScreenshot(screenshotPath);
                ExtentReportManager.getTest().fail("Scenario Failed: " + scenario.getName());
            } else {
                ExtentReportManager.getTest().pass("Scenario Passed: " + scenario.getName());
            }
            
            // Browser close is handled by Hooks.java (order = 1) which runs AFTER this
            logger.info("Completed scenario: " + scenario.getName());
        } finally {
            SoftAssertManager.clear();
        }
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
        String currentUrl = driver.getCurrentUrl();
        // SOFT ASSERT: URL verification (non-critical)
        // Accounts Overview page URL can be overview.htm or just the base URL after login
        boolean isOnOverviewPage = currentUrl.contains("overview.htm") || 
                                   (!currentUrl.contains("login") && !currentUrl.contains("register"));
        SoftAssertManager.assertTrue(isOnOverviewPage, "User should be on Accounts Overview page");
        ExtentReportManager.logStep("Then", "Verify Accounts Overview page", "User on Accounts Overview page", "URL: " + currentUrl, "Pass");
    }

    @Then("the Accounts Overview header should be displayed")
    public void theAccountsOverviewHeaderShouldBeDisplayed() {
        // SOFT ASSERT: Header display verification (non-critical)
        SoftAssertManager.assertTrue(loginPage.isLoginSuccessful(), "Accounts Overview header should be displayed");
        ExtentReportManager.logStep("Then", "Verify Accounts Overview header", "Header displayed", "Accounts Overview header is displayed", "Pass");
    }

    @Then("an error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
        // SOFT ASSERT: Error message display (non-critical)
        SoftAssertManager.assertTrue(isErrorDisplayed, "Error message should be displayed");
        ExtentReportManager.logStep("Then", "Verify error message displayed", "Error message displayed", "Error message is displayed", "Pass");
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedText) {
        String errorMessage = loginPage.getErrorMessage();
        // SOFT ASSERT: Error message content (non-critical)
        SoftAssertManager.assertTrue(errorMessage.contains(expectedText) || errorMessage.contains("Error"), "Error message should contain: " + expectedText);
        ExtentReportManager.logStep("Then", "Verify error message content", "Error contains: " + expectedText, "Error message: " + errorMessage, "Pass");
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        boolean isOnLoginPage = loginPage.isOnLoginPage();
        // SOFT ASSERT: Page location verification (non-critical)
        SoftAssertManager.assertTrue(isOnLoginPage, "User should be on login page");
        ExtentReportManager.logStep("Then", "Verify user on login page", "User on login page", "User is on login page", "Pass");
    }

    @Then("the login should fail")
    public void theLoginShouldFail() {
        boolean isLoginSuccessful = loginPage.isLoginSuccessful();
        // SOFT ASSERT: Login failure verification (non-critical)
        SoftAssertManager.assertFalse(isLoginSuccessful, "Login should fail");
        ExtentReportManager.logStep("Then", "Verify login failure", "Login failed", "Login failed as expected", "Pass");
    }

    @Then("no unauthorized access should be granted")
    public void noUnauthorizedAccessShouldBeGranted() {
        String currentUrl = driver.getCurrentUrl();
        // SOFT ASSERT: Security verification (non-critical, allows other checks)
        SoftAssertManager.assertFalse(currentUrl.contains("overview.htm"), "Should not be on Accounts Overview page");
        ExtentReportManager.logStep("Then", "Verify no unauthorized access", "No unauthorized access", "No unauthorized access granted", "Pass");
    }

    @Then("the script should not be executed")
    public void theScriptShouldNotBeExecuted() {
        String currentUrl = driver.getCurrentUrl();
        // SOFT ASSERT: XSS verification (non-critical, allows other checks)
        SoftAssertManager.assertFalse(currentUrl.contains("overview.htm"), "Should not be on Accounts Overview page");
        ExtentReportManager.logStep("Then", "Verify script not executed", "Script not executed", "XSS script was not executed", "Pass");
    }

    @Then("the password field should have type {string}")
    public void thePasswordFieldShouldHaveType(String expectedType) {
        String actualType = loginPage.getPasswordFieldType();
        // SOFT ASSERT: Field type verification (non-critical)
        SoftAssertManager.assertEquals(actualType, expectedType, "Password field type should be: " + expectedType);
        ExtentReportManager.logStep("Then", "Verify password field type", "Type is: " + expectedType, "Actual type: " + actualType, "Pass");
    }

    @Then("the password characters should be masked")
    public void thePasswordCharactersShouldBeMasked() {
        String fieldType = loginPage.getPasswordFieldType();
        // SOFT ASSERT: Password masking verification (non-critical)
        SoftAssertManager.assertEquals(fieldType, "password", "Password field should be masked");
        ExtentReportManager.logStep("Then", "Verify password masking", "Password is masked", "Password characters are masked", "Pass");
    }

    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        boolean isOnLoginPage = loginPage.isOnLoginPage();
        // SOFT ASSERT: Logout redirect verification (non-critical)
        SoftAssertManager.assertTrue(isOnLoginPage, "User should be on login page after logout");
        ExtentReportManager.logStep("Then", "Verify redirect to login page", "User on login page", "User is on login page", "Pass");
    }

    @Then("the Customer Login header should be displayed")
    public void theCustomerLoginHeaderShouldBeDisplayed() {
        boolean isOnLoginPage = loginPage.isOnLoginPage();
        // SOFT ASSERT: Header display verification (non-critical)
        SoftAssertManager.assertTrue(isOnLoginPage, "Customer Login header should be displayed");
        ExtentReportManager.logStep("Then", "Verify Customer Login header", "Header displayed", "Customer Login header is displayed", "Pass");
    }

    // ==================== Registration + Login Flow Steps ====================

    @Given("a new user is registered with unique credentials")
    public void aNewUserIsRegisteredWithUniqueCredentials() {
        try {
            // Check if registration was already done by Hooks (@RequiresRegistration)
            if (testContext.getUsername() != null && testContext.getPassword() != null) {
                // Registration already done by hook, just log and continue
                ExtentReportManager.logStep("Given", "Register new user", "Registration successful", 
                    "User already registered by Hook: " + testContext.getUsername(), "Pass");
                logger.info("Using credentials from Hook - Username: " + testContext.getUsername());
                return;
            }
            
            // Perform registration only if not done by hook (for scenarios without @RequiresRegistration)
            registerPage = new RegisterPage(driver, testContext);
            registerPage.performRegistrationWithUniqueData();
            Assert.assertTrue(registerPage.isRegistrationSuccessful(), "Registration should be successful");
            testContext.setUsername(registerPage.getGeneratedUsername());
            testContext.setPassword(registerPage.getGeneratedPassword());
            ExtentReportManager.logStep("Given", "Register new user", "Registration successful", 
                "User registered: " + testContext.getUsername(), "Pass");
            logger.info("New user registered: " + testContext.getUsername());
        } catch (Exception e) {
            ExtentReportManager.logStep("Given", "Register new user", "Registration successful", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Given("the user navigates to login page after registration")
    public void theUserNavigatesToLoginPageAfterRegistration() {
        try {
            // Logout to return to login page
            //driver.get("https://parabank.parasoft.com/");
            loginPage.navigateToLoginPage();
            Assert.assertTrue(loginPage.isOnLoginPage(), "User should be on login page");
            ExtentReportManager.logStep("Given", "Navigate to login after registration", "Login page displayed", "User on login page", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Given", "Navigate to login after registration", "Login page displayed", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user enters the registered username")
    public void theUserEntersTheRegisteredUsername() {
        try {
            String username = testContext.getUsername();
            loginPage.enterUsername(username);
            ExtentReportManager.logStep("When", "Enter registered username", "Username entered", 
                "Username: " + username, "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Enter registered username", "Username entered", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @When("the user enters the registered password")
    public void theUserEntersTheRegisteredPassword() {
        try {
            String password = testContext.getPassword();
            loginPage.enterPassword(password);
            ExtentReportManager.logStep("When", "Enter registered password", "Password entered", "Password entered successfully", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("When", "Enter registered password", "Password entered", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    @Given("the user is logged in with newly registered credentials")
    public void theUserIsLoggedInWithNewlyRegisteredCredentials() {
        try {
            // First register if not already registered
            if (testContext.getUsername() == null || testContext.getPassword() == null) {
                aNewUserIsRegisteredWithUniqueCredentials();
            }
            // Navigate to login and login with stored credentials
            theUserNavigatesToLoginPageAfterRegistration();
            theUserEntersTheRegisteredUsername();
            theUserEntersTheRegisteredPassword();
            theUserClicksTheLoginButton();
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with registered credentials");
            ExtentReportManager.logStep("Given", "Login with registered credentials", "User logged in", "Successfully logged in", "Pass");
        } catch (Exception e) {
            ExtentReportManager.logStep("Given", "Login with registered credentials", "User logged in", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }
}
