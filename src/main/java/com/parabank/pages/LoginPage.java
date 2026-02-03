package com.parabank.pages;

import com.parabank.utils.ExtentReportManager;
import com.parabank.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@name='username']")
    private WebElement usernameField;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@value='Log In']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@id='rightPanel']//h1[contains(text(),'Accounts Overview')]")
    private WebElement accountsOverviewHeader;

    @FindBy(xpath = "//div[@id='rightPanel']//p[@class='error']")
    private WebElement errorMessage;

    @FindBy(xpath = "//div[@id='rightPanel']//h1[contains(text(),'Welcome')]")
    private WebElement welcomeHeader;

    @FindBy(xpath = "//a[contains(text(),'Log Out')]")
    private WebElement logoutLink;

    @FindBy(xpath = "//div[@id='leftPanel']//h2[contains(text(),'Customer Login')]")
    private WebElement customerLoginHeader;

    @FindBy(xpath = "//div[@class='login']//input[@name='username']")
    private WebElement usernameInput;

    @FindBy(xpath = "//div[@class='login']//input[@name='password']")
    private WebElement passwordInput;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        try {
            driver.get("https://parabank.parasoft.com/");
            waitUtil.waitForElementToBeVisible(customerLoginHeader);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "LoginPage_Loaded");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("1", "Navigate to login page", "Login page displayed", "Login page loaded successfully", "Pass");
            logger.info("Navigated to login page");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "LoginPage_Load_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("1", "Navigate to login page", "Login page displayed", "Failed to load login page: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public void enterUsername(String username) {
        try {
            enterText(usernameField, username);
            ExtentReportManager.logStep("2", "Enter username", "Username entered: " + username, "Username entered successfully", "Pass");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EnterUsername_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("2", "Enter username", "Username entered: " + username, "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public void enterPassword(String password) {
        try {
            enterText(passwordField, password);
            ExtentReportManager.logStep("3", "Enter password", "Password entered (masked)", "Password entered successfully", "Pass");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "EnterPassword_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("3", "Enter password", "Password entered", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public void clickLoginButton() {
        try {
            clickElement(loginButton);
            ExtentReportManager.logStep("4", "Click Login button", "Login button clicked", "Login button clicked successfully", "Pass");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "ClickLogin_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("4", "Click Login button", "Login button clicked", "Failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public void performLogin(String username, String password) {
        navigateToLoginPage();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginSuccessful() {
        try {
            waitUtil.waitForElementToBeVisible(accountsOverviewHeader);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Login_Success");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("5", "Verify login success", "Accounts Overview page displayed", "Login successful - Accounts Overview displayed", "Pass");
            logger.info("Login successful - Accounts Overview page displayed");
            return true;
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Login_Verification_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("5", "Verify login success", "Accounts Overview page displayed", "Login failed or page not loaded: " + e.getMessage(), "Fail");
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            String errorText = getElementText(errorMessage);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Error_Message_Displayed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("5", "Verify error message", "Error message displayed", "Error: " + errorText, "Pass");
            logger.info("Error message displayed: " + errorText);
            return errorText;
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Error_Message_Not_Found");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("5", "Verify error message", "Error message displayed", "No error message found: " + e.getMessage(), "Fail");
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    public boolean isOnLoginPage() {
        return isElementDisplayed(customerLoginHeader);
    }

    public void clickLogout() {
        try {
            clickElement(logoutLink);
            waitUtil.waitForElementToBeVisible(customerLoginHeader);
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Logout_Success");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("Logout", "Click Logout link", "User logged out", "Logout successful", "Pass");
            logger.info("Logout successful");
        } catch (Exception e) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "Logout_Failed");
            ExtentReportManager.attachScreenshot(screenshotPath);
            ExtentReportManager.logStep("Logout", "Click Logout link", "User logged out", "Logout failed: " + e.getMessage(), "Fail");
            throw e;
        }
    }

    public String getPasswordFieldType() {
        return getAttributeValue(passwordField, "type");
    }

    public void clearUsernameField() {
        try {
            waitUtil.waitForElementToBeVisible(usernameField);
            usernameField.clear();
            logger.info("Username field cleared");
        } catch (Exception e) {
            logger.error("Failed to clear username field: " + e.getMessage());
            throw e;
        }
    }

    public void clearPasswordField() {
        try {
            waitUtil.waitForElementToBeVisible(passwordField);
            passwordField.clear();
            logger.info("Password field cleared");
        } catch (Exception e) {
            logger.error("Failed to clear password field: " + e.getMessage());
            throw e;
        }
    }
}
