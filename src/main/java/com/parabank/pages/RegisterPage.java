package com.parabank.pages;

import com.parabank.utils.DataGenerator;
import com.parabank.utils.ExtentReportManager;
import com.parabank.utils.ScreenshotUtil;
import com.parabank.utils.TestContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterPage extends BasePage {

    @FindBy(xpath = "//a[@href='register.htm']")
    private WebElement registerLink;

    @FindBy(id = "customer.firstName")
    private WebElement firstNameField;

    @FindBy(id = "customer.lastName")
    private WebElement lastNameField;

    @FindBy(id = "customer.address.street")
    private WebElement addressField;

    @FindBy(id = "customer.address.city")
    private WebElement cityField;

    @FindBy(id = "customer.address.state")
    private WebElement stateField;

    @FindBy(id = "customer.address.zipCode")
    private WebElement zipCodeField;

    @FindBy(id = "customer.phoneNumber")
    private WebElement phoneNumberField;

    @FindBy(id = "customer.ssn")
    private WebElement ssnField;

    @FindBy(id = "customer.username")
    private WebElement usernameField;

    @FindBy(id = "customer.password")
    private WebElement passwordField;

    @FindBy(id = "repeatedPassword")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//input[@value='Register']")
    private WebElement registerButton;

    @FindBy(xpath = "//div[@id='rightPanel']//h1[contains(text(),'Welcome')]")
    private WebElement welcomeHeader;

    @FindBy(xpath = "//div[@id='rightPanel']//p[contains(text(),'successfully')]")
    private WebElement successMessage;

    @FindBy(xpath = "//span[@id='customer.firstName.errors']")
    private WebElement firstNameError;

    @FindBy(xpath = "//span[@id='customer.username.errors']")
    private WebElement usernameError;

    @FindBy(xpath = "//a[@href='logout.htm']")
    private WebElement logOutButton;

    private TestContext testContext;

    public RegisterPage(WebDriver driver) {
        super(driver);
        this.testContext = new TestContext();
    }

    public RegisterPage(WebDriver driver, TestContext testContext) {
        super(driver);
        this.testContext = testContext;
    }

    public void navigateToRegisterPage() {
        try {
            driver.get("https://parabank.parasoft.com/");   
            clickElement(registerLink);
            waitUtil.waitForElementToBeVisible(firstNameField);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "RegisterPage_Loaded");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("1", "Navigate to register page", "Register page displayed", "Registration page loaded successfully", "Pass");
            logger.info("Navigated to registration page");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "RegisterPage_Load_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("1", "Navigate to register page", "Register page displayed", "Failed to load registration page: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public void clickRegisterLink() {
        try {
            clickElement(registerLink);
            waitUtil.waitForElementToBeVisible(firstNameField);
            logger.info("Clicked on Register link");
        } catch (Exception e) {
            logger.error("Failed to click Register link: " + e.getMessage());
            throw e;
        }
    }

    public void fillRegistrationFormWithUniqueData() {
        // Generate unique credentials and store in TestContext
        String uniqueUsername = DataGenerator.generateUniqueUsername();
        String uniquePassword = DataGenerator.generateUniquePassword();
        String firstName = DataGenerator.generateFirstName();
        String lastName = DataGenerator.generateLastName();

        testContext.setUsername(uniqueUsername);
        testContext.setPassword(uniquePassword);
        testContext.setFirstName(firstName);
        testContext.setLastName(lastName);

        // Fill the form
        enterText(firstNameField, firstName);
        enterText(lastNameField, lastName);
        enterText(addressField, DataGenerator.generateStreetAddress());
        enterText(cityField, DataGenerator.generateCity());
        enterText(stateField, DataGenerator.generateState());
        enterText(zipCodeField, DataGenerator.generateZipCode());
        enterText(phoneNumberField, DataGenerator.generatePhoneNumber());
        enterText(ssnField, DataGenerator.generateSSN());
        enterText(usernameField, uniqueUsername);
        enterText(passwordField, uniquePassword);
        enterText(confirmPasswordField, uniquePassword);

        String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Registration_Form_Filled");
        ExtentReportManager.attachScreenshot(screenshotPath);
        ExtentReportManager.logStep("2", "Fill registration form", "Form filled with unique data", 
            "Username: " + uniqueUsername + ", Password: [masked]", "Pass");
        logger.info("Registration form filled with unique username: " + uniqueUsername);
    }

    public void submitRegistration() {
        try {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Before_Registration_Submit");
            ExtentReportManager.attachScreenshot(screenshotPath);
            clickElement(registerButton);
            ExtentReportManager.logStep("3", "Submit registration", "Registration submitted", "Registration form submitted", "Pass");
            logger.info("Registration form submitted");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Registration_Submit_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("3", "Submit registration", "Registration submitted", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public void logoutTheUser() {
        try {
            clickElement(logOutButton);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "After_Logout");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("3", "logout the user", "User to ne logged out", "User loged out successfully", "Pass");
            logger.info("Registration form submitted");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Registration_Submit_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("3", "Submit registration", "Registration submitted", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public boolean isRegistrationSuccessful() {
        try {
            waitUtil.waitForElementToBeVisible(welcomeHeader);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Registration_Success");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("4", "Verify registration success", "Welcome page displayed", 
                "Registration successful for user: " + testContext.getUsername(), "Pass");
            logger.info("Registration successful for user: " + testContext.getUsername());
            return true;
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Registration_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("4", "Verify registration success", "Welcome page displayed", 
                "Registration failed: " + e.getMessage(), "Fail");
            logger.error("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public boolean isUsernameErrorDisplayed() {
        try {
            return usernameError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public TestContext getTestContext() {
        return testContext;
    }

    public String getGeneratedUsername() {
        return testContext.getUsername();
    }

    public String getGeneratedPassword() {
        return testContext.getPassword();
    }

    public void performRegistrationWithUniqueData() {
        navigateToRegisterPage();
        fillRegistrationFormWithUniqueData();
        submitRegistration();
    }
}
