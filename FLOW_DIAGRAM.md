# ParaBank Automation Framework - Flow Diagrams

This document contains detailed flow diagrams illustrating the architecture and execution flow of the ParaBank BDD Automation Framework.

---

## 🏗️ High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              TEST AUTOMATION FRAMEWORK                               │
└─────────────────────────────────────────────────────────────────────────────────────┘

    ┌─────────────┐         ┌─────────────┐         ┌─────────────┐
    │   TESTERS   │────────▶│   MAVEN     │────────▶│   TESTNG    │
    │  & DEVOPS   │         │    BUILD    │         │   RUNNER    │
    └─────────────┘         └─────────────┘         └──────┬──────┘
                                                           │
                                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              CUCUMBER BDD LAYER                                      │
│  ┌─────────────────────────────────────────────────────────────────────────────┐   │
│  │  Feature Files (Gherkin)                                                     │   │
│  │  ├── Login.feature                                                          │   │
│  │  │   ├── Scenario: Successful Login                                         │   │
│  │  │   ├── Scenario: Invalid Password                                         │   │
│  │  │   ├── Scenario: SQL Injection Test                                       │   │
│  │  │   └── Scenario: XSS Test                                                 │   │
│  │  │                                                                         │   │
│  │  └── Tags: @Smoke @Regression @Security @Negative @Critical               │   │
│  └─────────────────────────────────────────────────────────────────────────────┘   │
│                                      │                                              │
│                                      ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────────────┐   │
│  │  Step Definitions                                                            │   │
│  │  ├── LoginStepDefinitions.java                                              │   │
│  │  │   ├── @Given("user is on login page")                                    │   │
│  │  │   ├── @When("user enters username")                                     │   │
│  │  │   └── @Then("user should see dashboard")                                │   │
│  │  │                                                                         │   │
│  │  └── Hooks.java                                                             │   │
│  │      ├── @Before("@RequiresRegistration") → Auto-register user             │   │
│  │      └── @After → Cleanup & Report                                          │   │
│  └─────────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                                           │
                                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              PAGE OBJECT MODEL LAYER                                 │
│                                                                                      │
│   ┌─────────────┐         ┌─────────────┐         ┌─────────────┐                  │
│   │  BasePage   │◀────────│  LoginPage  │         │ RegisterPage│                  │
│   │  (Abstract) │         │             │         │             │                  │
│   │             │         │ - username  │         │ - firstName │                  │
│   │ - driver    │         │ - password  │         │ - lastName  │                  │
│   │ - waitUtil  │         │ - loginBtn  │         │ - username  │                  │
│   │ - logger    │         │             │         │ - password  │                  │
│   │             │         │ + enterUser()│        │             │                  │
│   │ + click()   │         │ + enterPass()│        │ + fillForm()│                  │
│   │ + enterText()│        │ + clickLogin()│       │ + submit()  │                  │
│   │ + getText() │         │ + isSuccess()│        │ + isSuccess()│                 │
│   └─────────────┘         └─────────────┘         └─────────────┘                  │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                                           │
                                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              UTILITIES LAYER                                         │
│                                                                                      │
│   ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│   │WebDriver │  │ Config   │  │  Wait    │  │Screenshot│  │  Extent  │             │
│   │ Factory  │  │  Reader  │  │   Util   │  │   Util   │  │  Report  │             │
│   │          │  │          │  │          │  │          │  │ Manager  │             │
│   │ThreadLocal│  │Properties│  │Explicit  │  │ Capture  │  │  HTML    │             │
│   │- Chrome  │  │- Base URL│  │- Fluent  │  │- Auto on │  │- Steps   │             │
│   │- Firefox │  │- Browser │  │- Custom  │  │  Fail    │  │- Screens │             │
│   │- Edge    │  │- Timeouts│  │          │  │          │  │          │             │
│   └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘             │
│                                                                                      │
│   ┌──────────┐  ┌──────────┐  ┌──────────┐                                          │
│   │   Data   │  │   Test   │  │   Soft   │                                          │
│   │ Generator│  │  Context │  │  Assert  │                                          │
│   │          │  │          │  │ Manager  │                                          │
│   │- Unique  │  │- Share   │  │- Multiple│                                          │
│   │  Username│  │  Data    │  │  Checks  │                                          │
│   │- Password│  │- Cross   │  │- Collect │                                          │
│   │- Address │  │  Steps   │  │  All     │                                          │
│   └──────────┘  └──────────┘  └──────────┘                                          │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                                           │
                                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                           EXTERNAL SERVICES                                          │
│                                                                                      │
│        ┌─────────────────┐              ┌─────────────────┐                         │
│        │  PARABANK WEB   │              │  WEBDRIVER      │                         │
│        │  APPLICATION    │              │  MANAGER        │                         │
│        │                 │              │                 │                         │
│        │ https://        │              │ - Auto-download │                         │
│        │ parabank.       │              │ - ChromeDriver  │                         │
│        │ parasoft.com    │              │ - GeckoDriver   │                         │
│        │                 │              │ - EdgeDriver    │                         │
│        └─────────────────┘              └─────────────────┘                         │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Test Execution Flow

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                    AUTH-001: SUCCESSFUL LOGIN FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────────────┘

    START
      │
      ▼
┌─────────────────┐
│  Maven Command  │
│  mvn clean test │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  PHASE 1: INITIALIZATION                                                             │
│  ════════════════════════                                                            │
│                                                                                      │
│  1.1 Load ConfigReader                                                               │
│      ├── Read config.properties                                                      │
│      ├── Get browser=chrome                                                          │
│      └── Get base.url=https://parabank.parasoft.com/                                │
│                                                                                      │
│  1.2 Initialize ExtentReports                                                        │
│      ├── Create Report Directory: test-output/reports/                              │
│      └── Set System Info (OS, Java, Browser)                                        │
│                                                                                      │
│  1.3 Initialize WebDriver                                                            │
│      ├── WebDriverManager.chromedriver().setup()                                    │
│      ├── ChromeOptions: --start-maximized, --disable-notifications                  │
│      ├── Set Implicit Wait: 10s                                                     │
│      └── Set Page Load Timeout: 30s                                                 │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  PHASE 2: HOOK EXECUTION (@Before @RequiresRegistration)                            │
│  ═══════════════════════════════════════════════════════                             │
│                                                                                      │
│  2.1 Create Test in ExtentReport                                                     │
│      └── Test Name: "AUTH-001 - Successful login after registration"                │
│                                                                                      │
│  2.2 Initialize SoftAssertManager                                                    │
│      └── ThreadLocal<SoftAssert> initialized                                        │
│                                                                                      │
│  2.3 AUTO-REGISTRATION (Tagged with @RequiresRegistration)                           │
│      │                                                                               │
│      ├── Navigate to https://parabank.parasoft.com/                                 │
│      │                                                                               │
│      ├── Click "Register" Link                                                       │
│      │                                                                               │
│      ├── Fill Registration Form (DataGenerator)                                      │
│      │   ├── First Name:  "John" (random)                                           │
│      │   ├── Last Name:   "Smith" (random)                                          │
│      │   ├── Address:     "123 Main St"                                             │
│      │   ├── City:        "New York"                                                │
│      │   ├── State:       "NY"                                                      │
│      │   ├── Zip Code:    "10001"                                                   │
│      │   ├── Phone:       "555-123-4567"                                            │
│      │   ├── SSN:         "123-45-6789"                                             │
│      │   ├── Username:    "user1707312345" ← Unique timestamp                       │
│      │   └── Password:    "Pass@1707312345" ← Unique timestamp                      │
│      │                                                                               │
│      ├── Submit Registration                                                         │
│      │                                                                               │
│      ├── Verify Success Message                                                      │
│      │   └── Assert: "Your account was created successfully" is displayed           │
│      │        [HARD ASSERT - Critical]                                               │
│      │                                                                               │
│      ├── Store Credentials in TestContext                                            │
│      │   ├── testContext.setUsername("user1707312345")                              │
│      │   └── testContext.setPassword("Pass@1707312345")                             │
│      │                                                                               │
│      └── Logout (Navigate to logout.htm)                                             │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  PHASE 3: SCENARIO EXECUTION (Gherkin Steps)                                         │
│  ═══════════════════════════════════════════                                         │
│                                                                                      │
│  Scenario: AUTH-001 - Successful login after registration                           │
│  Tags: @Smoke @Regression @HighPriority @RequiresRegistration @Register             │
│                                                                                      │
│  Step 1: Given a new user is registered with unique credentials                     │
│          └── Status: SKIPPED (Already done in @Before hook)                         │
│                                                                                      │
│  Step 2: Given the user navigates to login page after registration                  │
│          ├── Action: driver.get("https://parabank.parasoft.com/")                  │
│          ├── Wait: waitForElementToBeVisible(customerLoginHeader)                  │
│          ├── Screenshot: "LoginPage_Loaded"                                         │
│          └── Status: PASS                                                           │
│                                                                                      │
│  Step 3: When the user enters the registered username                               │
│          ├── Action: loginPage.enterUsername(testContext.getUsername())            │
│          ├── Value: "user1707312345"                                                │
│          └── Status: PASS                                                           │
│                                                                                      │
│  Step 4: When the user enters the registered password                               │
│          ├── Action: loginPage.enterPassword(testContext.getPassword())            │
│          ├── Value: "Pass@1707312345" (masked in logs)                             │
│          └── Status: PASS                                                           │
│                                                                                      │
│  Step 5: When the user clicks the Login button                                      │
│          ├── Screenshot: "Login_Details_Entered"                                    │
│          ├── Action: loginPage.clickLoginButton()                                   │
│          └── Status: PASS                                                           │
│                                                                                      │
│  Step 6: Then the user should be redirected to the Accounts Overview page           │
│          ├── Verification: URL contains "overview.htm"                              │
│          ├── Type: SOFT ASSERT (continues if fails)                                 │
│          └── Status: PASS                                                           │
│                                                                                      │
│  Step 7: Then the Accounts Overview header should be displayed                      │
│          ├── Verification: accountsOverviewHeader is visible                        │
│          ├── Screenshot: "Login_Success"                                            │
│          └── Status: PASS                                                           │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  PHASE 4: TEARDOWN (@After)                                                          │
│  ══════════════════════════                                                          │
│                                                                                      │
│  4.1 Assert All Soft Assertions                                                      │
│      └── SoftAssertManager.assertAll()                                              │
│          └── If any soft assertion failed → Mark test as FAIL                       │
│                                                                                      │
│  4.2 Update Extent Report                                                            │
│      ├── If PASSED: test.pass("Scenario Passed: AUTH-001...")                       │
│      └── If FAILED: test.fail("Scenario Failed: AUTH-001...")                       │
│                      + Attach failure screenshot                                     │
│                                                                                      │
│  4.3 Cleanup                                                                         │
│      ├── WebDriverFactory.quitDriver() → Close browser                              │
│      ├── TestContext.clear() → Remove stored credentials                            │
│      └── SoftAssertManager.clear() → Reset soft assertions                          │
│                                                                                      │
│  4.4 Log Completion                                                                  │
│      └── logger.info("Completed scenario: AUTH-001...")                             │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
    ┌────────┐
    │  END   │
    └────────┘
```

---

## 🔒 Security Testing Flow

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                        SECURITY TEST IMPLEMENTATION                                  │
└─────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────┐      ┌─────────────────────────────────┐
│      SQL INJECTION TEST         │      │           XSS TEST              │
│         (AUTH-008)              │      │          (AUTH-009)             │
└─────────────────────────────────┘      └─────────────────────────────────┘
             │                                        │
             ▼                                        ▼
┌─────────────────────────┐              ┌─────────────────────────┐
│ Input Malicious Payload │              │ Input Malicious Script  │
│                         │              │                         │
│ Username:               │              │ Username:               │
│ ' OR '1'='1             │              │ <script>alert('xss')    │
│                         │              │ </script>               │
└───────────┬─────────────┘              └───────────┬─────────────┘
            │                                        │
            ▼                                        ▼
┌─────────────────────────┐              ┌─────────────────────────┐
│ Application Processes   │              │ Application Processes   │
│ Input                   │              │ Input                   │
│                         │              │                         │
│ Expected Behavior:      │              │ Expected Behavior:      │
│ • Input sanitized       │              │ • Input sanitized       │
│ • Query not modified    │              │ • Script not executed   │
│ • Authentication fails  │              │ • Treated as plain text │
└───────────┬─────────────┘              └───────────┬─────────────┘
            │                                        │
            ▼                                        ▼
┌─────────────────────────┐              ┌─────────────────────────┐
│ Verify Security Control │              │ Verify Security Control │
│                         │              │                         │
│ Assertions:             │              │ Assertions:             │
│ ✗ Login fails           │              │ ✗ Login fails           │
│ ✗ No dashboard access   │              │ ✗ No dashboard access   │
│ ✗ Error message shown   │              │ ✗ Error message shown   │
│ ✓ SQL neutralized       │              │ ✓ Script neutralized    │
└───────────┬─────────────┘              └───────────┬─────────────┘
            │                                        │
            └────────────────┬───────────────────────┘
                             │
                             ▼
                 ┌─────────────────────┐
                 │   TEST RESULT       │
                 │                     │
                 │ PASS = Secure ✓     │
                 │ FAIL = Vulnerable ✗ │
                 └─────────────────────┘
```

---

## 📊 Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                           DATA FLOW ACROSS LAYERS                                    │
└─────────────────────────────────────────────────────────────────────────────────────┘

   FEATURE FILE                    STEP DEFINITIONS               PAGE OBJECTS
   ────────────                    ────────────────               ────────────

   Login.feature                   LoginStepDefinitions.java      LoginPage.java
   │                               │                              │
   │  Scenario:                    │  public void                 │  public void
   │  Successful Login             │  theUserEntersUsername()     │  enterUsername()
   │       │                       │       │                      │       │
       │                           │       │                      │       │
       ▼                           │       ▼                      │       ▼
   "user enters                    │  loginPage.                  │  enterText(
    username"                      │  enterUsername(               │    usernameField,
       │                           │    username);                 │    text);
       │                           │       │                      │       │
       │                           │       │                      │       ▼
       │                           │       │                      │  waitUtil.
       │                           │       │                      │    waitForElement()
       │                           │       │                      │       │
       │                           │       │                      │       ▼
       │                           │       │                      │  element.sendKeys()
       │                           │       │                            │
       │                           │       │                            ▼
       │                           │       │                      ExtentReportManager
       │                           │       │                        .logStep()
       │                           │       │                            │
       │                           │       │                            ▼
       │                           │       │                      ScreenshotUtil
       │                           │       │                        .captureScreenshot()
       │                           │       │                            │
       │                           │       │◀───────────────────────────┘
       │                           │       │
       │                           │       ▼
       │                           │  ExtentReportManager
       │                           │    .logStep()
       │                           │       │
       │◀──────────────────────────┘       │
       │                                   │
       ▼                                   ▼
   Step Result                        Test Report Updated


   UTILITIES (Supporting Data Flow)
   ───────────────────────────────

   ConfigReader ───────▶ Properties File (config.properties)
        │
        ├── base.url
        ├── browser
        └── timeouts

   DataGenerator ──────▶ Random Test Data
        │
        ├── generateUniqueUsername() → "user1707312345"
        ├── generateUniquePassword() → "Pass@1707312345"
        ├── generateFirstName()      → "John"
        └── generateSSN()            → "123-45-6789"

   TestContext ◀──────▶ Shared Data Between Steps
        │
        ├── setUsername() / getUsername()
        ├── setPassword() / getPassword()
        └── clear() [After scenario]

   SoftAssertManager ──▶ Multiple Validation Results
        │
        ├── assertTrue()  [Collects pass/fail]
        ├── assertFalse() [Collects pass/fail]
        ├── assertEquals() [Collects pass/fail]
        └── assertAll()   [Reports all at end]
```

---

## 📁 Directory Structure Flow

```
ParaBankSelenium_1600/
│
├── src/main/java/com/parabank/
│   │
│   ├── pages/                           Page Object Model
│   │   ├── BasePage.java               ───────┐
│   │   ├── LoginPage.java                     │ Extends
│   │   └── RegisterPage.java         ─────────┘
│   │
│   └── utils/                           Utility Classes
│       ├── WebDriverFactory.java       ───┐
│       ├── ConfigReader.java              │ Used by
│       ├── DataGenerator.java             │ All Pages
│       ├── ExtentReportManager.java       │ & Steps
│       ├── ScreenshotUtil.java            │
│       ├── SoftAssertManager.java         │
│       ├── TestContext.java               │
│       └── WaitUtil.java               ───┘
│
├── src/test/java/com/parabank/
│   │
│   ├── runners/
│   │   └── CucumberTestRunner.java     ───┐
│   │                                      │ Triggers
│   └── stepdefinitions/                   │
│       ├── Hooks.java                  ───┤
│       └── LoginStepDefinitions.java      │
│                                          │ Calls
├── src/test/resources/                    │
│   ├── features/                          │
│   │   └── Login.feature             ─────┤ Gherkin
│   │                                      │ Scenarios
│   └── config/                            │
│       ├── config.properties         ─────┤ Config
│       └── log4j2.xml                     │
│                                       ───┘
│
└── test-output/                         Reports & Screenshots
    ├── reports/
    │   └── ParaBank_Automation_Report_<timestamp>.html
    └── screenshots/
        └── <test_name>_<timestamp>.png
```

---

## 🔄 CI/CD Integration Flow

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         CI/CD INTEGRATION (Suggested)                                │
└─────────────────────────────────────────────────────────────────────────────────────┘

   Git Push
      │
      ▼
┌─────────────┐
│   Jenkins   │
│   /GitHub   │
│   Actions   │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  BUILD PHASE                                                                         │
│  ───────────                                                                         │
│  git clone → mvn clean compile → mvn test-compile                                   │
└─────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  TEST EXECUTION PHASE                                                                │
│  ─────────────────────                                                               │
│  │                                                                                  │
│  ├──▶ Smoke Tests (@Smoke)                                                          │
│  │     └── mvn test -Dcucumber.filter.tags="@Smoke"                                 │
│  │          │                                                                        │
│  │          └──▶ If PASS → Continue                                                  │
│  │          └──▶ If FAIL → Stop Pipeline                                             │
│  │                                                                                  │
│  ├──▶ Security Tests (@Security)                                                    │
│  │     └── mvn test -Dcucumber.filter.tags="@Security"                              │
│  │                                                                                  │
│  └──▶ Regression Tests (@Regression)                                                │
│        └── mvn test -Dcucumber.filter.tags="@Regression"                           │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  REPORTING PHASE                                                                     │
│  ───────────────                                                                     │
│  │                                                                                  │
│  ├── Generate Extent Report → Publish to HTML                                       │
│  │                                                                                  │
│  ├── Archive Screenshots → Attach to Build                                          │
│  │                                                                                  │
│  └── Cucumber JSON → Generate Living Documentation                                  │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│  NOTIFICATION                                                                        │
│  ────────────                                                                        │
│  Send Email/Slack:                                                                   │
│  • Test Summary (Pass/Fail/Skip)                                                    │
│  • Extent Report Link                                                               │
│  • Failed Test Screenshots                                                          │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 📈 Test Result Processing Flow

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                        TEST RESULT PROCESSING                                        │
└─────────────────────────────────────────────────────────────────────────────────────┘

                         Test Execution
                              │
           ┌──────────────────┼──────────────────┐
           │                  │                  │
           ▼                  ▼                  ▼
      ┌─────────┐       ┌─────────┐       ┌─────────┐
      │  PASS   │       │  FAIL   │       │  SKIP   │
      └────┬────┘       └────┬────┘       └────┬────┘
           │                  │                  │
           ▼                  ▼                  ▼
    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
    │ • Green dot │    │ • Red dot   │    │ • Yellow    │
    │ • Success   │    │ • Screenshot│    │   dot       │
    │   message   │    │   captured  │    │ • Ignored   │
    │ • Log step  │    │ • Stack     │    │   tag       │
    │   as Pass   │    │   trace     │    │ • Log step  │
    │             │    │ • Log step  │    │   as Skip   │
    │             │    │   as Fail   │    │             │
    └──────┬──────┘    └──────┬──────┘    └──────┬──────┘
           │                  │                  │
           └──────────────────┼──────────────────┘
                              │
                              ▼
                 ┌─────────────────────┐
                 │  Extent Report      │
                 │  HTML Generation    │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │  Final Report       │
                 │  ────────────       │
                 │  • Total Tests: 9   │
                 │  • Passed: 8        │
                 │  • Failed: 1        │
                 │  • Skipped: 0       │
                 │  • Pass %: 88.89%   │
                 │                     │
                 │  + Screenshots      │
                 │  + Step Details     │
                 │  + Execution Time   │
                 └─────────────────────┘
```

---

## 🎯 Summary

This flow diagram document illustrates:

1. **High-Level Architecture** - The four-layer structure of the framework
2. **Test Execution Flow** - Step-by-step execution for login scenarios
3. **Security Testing** - How SQL injection and XSS tests work
4. **Data Flow** - How data moves across layers
5. **Directory Structure** - Organization of the codebase
6. **CI/CD Integration** - Suggested pipeline integration
7. **Result Processing** - How test results are handled and reported

For more details, refer to the [README.md](README.md) file.
