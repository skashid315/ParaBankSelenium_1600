# ParaBank BDD Automation Framework

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.28.1-green.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.14.0-brightgreen.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-orange.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-purple.svg)](https://maven.apache.org/)

A comprehensive **Behavior-Driven Development (BDD)** automation framework for testing the **ParaBank Login Module** using Selenium WebDriver, Cucumber, and TestNG.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Architecture](#project-architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running Tests](#running-tests)
- [Test Scenarios](#test-scenarios)
- [Reporting](#reporting)
- [Framework Flow](#framework-flow)
- [Directory Structure](#directory-structure)
- [Configuration](#configuration)
- [Contributing](#contributing)

---

## 🔍 Overview

This automation framework tests the **ParaBank Login Module** (https://parabank.parasoft.com/) with a focus on:

- **Functional Testing**: Valid and invalid login scenarios
- **Security Testing**: SQL Injection and XSS vulnerability checks
- **Regression Testing**: Comprehensive test coverage with tags
- **Dynamic Data Generation**: Unique user registration for each test run
- **Detailed Reporting**: Extent Reports with screenshots and step-by-step logs

The framework implements the **Page Object Model (POM)** design pattern combined with **BDD** principles using Cucumber Gherkin syntax for readable, maintainable test scenarios.

---

## ✨ Features

### Core Features
| Feature | Description |
|---------|-------------|
| 🔄 **BDD Approach** | Gherkin syntax for readable test scenarios |
| 🏗️ **Page Object Model** | Maintainable page classes with Page Factory |
| 🏷️ **Test Tagging** | Run selective tests using Cucumber tags (@Smoke, @Regression, etc.) |
| 📸 **Screenshot Capture** | Automatic screenshots on failure and key steps |
| 📊 **Extent Reports** | Beautiful HTML reports with step details and screenshots |
| 📝 **Log4j2 Logging** | Comprehensive logging for debugging |
| ✅ **Soft Assertions** | Continue test execution while collecting all failures |
| 🔐 **Security Testing** | SQL Injection and XSS attack simulation |
| 🎲 **Dynamic Data** | Unique user generation for registration tests |
| 🧵 **Thread-Safe** | ThreadLocal implementation for parallel execution |

### Browser Support
- ✅ Google Chrome
- ✅ Mozilla Firefox
- ✅ Microsoft Edge

---

## 🛠️ Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming Language |
| Selenium WebDriver | 4.28.1 | Browser Automation |
| Cucumber JVM | 7.14.0 | BDD Framework |
| TestNG | 7.10.2 | Test Execution & Assertions |
| Extent Reports | 5.1.1 | HTML Test Reporting |
| WebDriverManager | 5.6.2 | Driver Management |
| Log4j2 | 2.22.0 | Logging Framework |
| Apache POI | 5.2.5 | Excel Data Handling |
| Maven | 3.9+ | Build Tool |

---

## 🏗️ Project Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    TEST EXECUTION LAYER                         │
├─────────────────────────────────────────────────────────────────┤
│  CucumberTestRunner.java  →  TestNG Suite  →  Maven Surefire    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BDD FEATURE LAYER                            │
├─────────────────────────────────────────────────────────────────┤
│  Login.feature  →  Gherkin Scenarios  →  Tags (@Smoke, etc.)   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                  STEP DEFINITION LAYER                          │
├─────────────────────────────────────────────────────────────────┤
│  LoginStepDefinitions.java  →  Step Methods  →  Hooks          │
│  Hooks.java  →  @Before/@After  →  Setup/Teardown              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   PAGE OBJECT LAYER                             │
├─────────────────────────────────────────────────────────────────┤
│  BasePage.java  →  Common Methods                               │
│  LoginPage.java  →  Login Actions & Verifications               │
│  RegisterPage.java  →  Registration Actions                     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   UTILITIES LAYER                               │
├─────────────────────────────────────────────────────────────────┤
│  WebDriverFactory  →  Browser Initialization                    │
│  ConfigReader  →  Properties Management                         │
│  ExtentReportManager  →  Report Generation                      │
│  ScreenshotUtil  →  Screenshot Capture                          │
│  WaitUtil  →  Explicit Waits                                    │
│  DataGenerator  →  Dynamic Test Data                            │
│  TestContext  →  Scenario Data Sharing                          │
│  SoftAssertManager  →  Soft Assertions                          │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📋 Prerequisites

- **Java JDK 17** or higher
- **Maven 3.9+** installed
- **Git** for version control
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)
- **Chrome/Firefox/Edge** browser installed

### Verify Installation
```bash
java -version
mvn -version
git --version
```

---

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ParaBankSelenium_1600
```

### 2. Install Dependencies
```bash
mvn clean install -DskipTests
```

### 3. Configure Test Data
Edit `src/test/resources/config/config.properties`:
```properties
base.url=https://parabank.parasoft.com/
browser=chrome
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Static credentials for negative tests
valid.username=your_username
valid.password=your_password
```

---

## ▶️ Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Smoke Tests Only
```bash
mvn clean test -Dcucumber.filter.tags="@Smoke"
```

### Run Regression Tests
```bash
mvn clean test -Dcucumber.filter.tags="@Regression"
```

### Run Tests by Priority
```bash
# Critical priority tests
mvn clean test -Dcucumber.filter.tags="@Critical"

# High priority tests
mvn clean test -Dcucumber.filter.tags="@HighPriority"
```

### Run Security Tests
```bash
mvn clean test -Dcucumber.filter.tags="@Security"
```

### Run Tests with Specific Browser
```bash
# Chrome (default)
mvn clean test -Dbrowser=chrome

# Firefox
mvn clean test -Dbrowser=firefox

# Edge
mvn clean test -Dbrowser=edge
```

### Run from TestNG XML
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
```

---

## 🧪 Test Scenarios

### Test Coverage Summary

| Test ID | Scenario | Tags | Priority |
|---------|----------|------|----------|
| AUTH-001 | Successful login after registration | @Smoke @Regression @HighPriority | High |
| AUTH-002 | Login with invalid password | @Regression @Negative | High |
| AUTH-003 | Login with non-existent username | @Regression @Negative | High |
| AUTH-004 | Login with empty username field | @Regression @Negative | Medium |
| AUTH-005 | Login with empty password field | @Regression @Negative | Medium |
| AUTH-008 | SQL Injection attempt in username | @Regression @Security @Critical | Critical |
| AUTH-009 | XSS attempt in username | @Regression @Security @Critical | Critical |
| AUTH-012 | Password masking verification | @Regression @Functional | Medium |
| AUTH-013 | Successful logout after login | @Smoke @Regression @Functional | High |

### Available Tags

| Tag | Description |
|-----|-------------|
| `@Smoke` | Smoke test suite |
| `@Regression` | Full regression suite |
| `@Negative` | Negative test cases |
| `@Security` | Security vulnerability tests |
| `@Functional` | Functional test cases |
| `@HighPriority` | High priority tests |
| `@MediumPriority` | Medium priority tests |
| `@Critical` | Critical tests |
| `@RequiresRegistration` | Tests needing user registration |

---

## 📊 Reporting

### Extent HTML Report
After test execution, reports are generated at:
```
test-output/reports/ParaBank_Automation_Report_<timestamp>.html
```

**Report Features:**
- Test execution summary (Pass/Fail/Skip count)
- Step-by-step execution details with timestamps
- Screenshots embedded for each step
- System environment information
- Search and filter capabilities

### Cucumber Reports
- **HTML Report**: `target/cucumber-reports/cucumber-html-report.html`
- **JSON Report**: `target/cucumber-reports/cucumber.json`
- **JUnit XML**: `target/cucumber-reports/cucumber.xml`

### Screenshots
Screenshots are captured automatically and saved to:
```
test-output/screenshots/
```

### Viewing Reports
```bash
# Open Extent Report
start test-output/reports/ParaBank_Automation_Report_*.html

# Open Cucumber HTML Report
start target/cucumber-reports/cucumber-html-report.html
```

---

## 🔄 Framework Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           TEST EXECUTION FLOW                                │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Maven      │────▶│   TestNG     │────▶│  Cucumber    │────▶│   Hooks      │
│  Surefire    │     │   Runner     │     │   Runner     │     │  @Before     │
└──────────────┘     └──────────────┘     └──────────────┘     └──────┬───────┘
                                                                      │
                              ┌───────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        REGISTRATION (if tagged)                              │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐  │
│  │  Navigate   │───▶│ Fill Form   │───▶│   Submit    │───▶│   Verify    │  │
│  │    to       │    │ Unique Data │    │  Registration│   │   Success   │  │
│  │  Register   │    │             │    │             │    │             │  │
│  └─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘  │
│                                      │                                      │
│                                      ▼                                      │
│                              ┌─────────────┐                               │
│                              │ Store Creds │                               │
│                              │  TestContext│                               │
│                              └─────────────┘                               │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           FEATURE EXECUTION                                  │
│                                                                              │
│   Feature: ParaBank Login Module                                             │
│   ├── Scenario: AUTH-001 - Successful login                                  │
│   │   ├── Given: Navigate to Login Page                                     │
│   │   ├── When: Enter Username/Password                                     │
│   │   ├── And: Click Login Button                                           │
│   │   └── Then: Verify Accounts Overview Page                               │
│   │                                                                          │
│   ├── Scenario: AUTH-002 - Invalid password                                  │
│   │   ├── When: Enter invalid credentials                                   │
│   │   └── Then: Verify error message displayed                              │
│   │                                                                          │
│   └── Scenario: AUTH-008 - SQL Injection                                     │
│       ├── When: Enter SQL injection payload                                 │
│       └── Then: Verify login fails, no unauthorized access                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           REPORTING & CLEANUP                                │
│                                                                              │
│   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐│
│   │   Capture    │   │    Log to    │   │   SoftAssert │   │    Flush     ││
│   │  Screenshot  │──▶│Extent Reports│──▶│  assertAll() │──▶│   Reports    ││
│   │              │   │              │   │              │   │              ││
│   └──────────────┘   └──────────────┘   └──────────────┘   └──────────────┘│
│                                                                              │
│   Hooks @After ──▶ WebDriver.quit() ──▶ TestContext.clear()                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📁 Directory Structure

```
ParaBankSelenium_1600/
│
├── 📂 src/
│   ├── 📂 main/java/com/parabank/
│   │   ├── 📂 pages/                    # Page Object Classes
│   │   │   ├── BasePage.java            # Base class with common methods
│   │   │   ├── LoginPage.java           # Login page actions
│   │   │   └── RegisterPage.java        # Registration page actions
│   │   │
│   │   └── 📂 utils/                    # Utility Classes
│   │       ├── ConfigReader.java        # Configuration properties reader
│   │       ├── DataGenerator.java       # Dynamic test data generation
│   │       ├── ExtentReportManager.java # HTML report generation
│   │       ├── ScreenshotUtil.java      # Screenshot capture utility
│   │       ├── SoftAssertManager.java   # Soft assertion handling
│   │       ├── TestContext.java         # Scenario data sharing
│   │       ├── WaitUtil.java            # Explicit wait utilities
│   │       └── WebDriverFactory.java    # Browser driver factory
│   │
│   └── 📂 test/
│       ├── 📂 java/com/parabank/
│       │   ├── 📂 runners/
│       │   │   └── CucumberTestRunner.java    # TestNG Cucumber runner
│       │   │
│       │   └── 📂 stepdefinitions/
│       │       ├── Hooks.java                 # Setup/teardown hooks
│       │       └── LoginStepDefinitions.java  # Step definitions
│       │
│       └── 📂 resources/
│           ├── 📂 config/
│           │   ├── config.properties    # Test configuration
│           │   └── log4j2.xml           # Logging configuration
│           │
│           ├── 📂 features/
│           │   └── Login.feature        # Gherkin feature file
│           │
│           ├── testng.xml               # TestNG suite configuration
│           ├── cucumber.properties      # Cucumber properties
│           └── extent.properties        # Extent report properties
│
├── 📂 test-output/                      # Test execution outputs
│   ├── 📂 reports/                      # Extent HTML reports
│   └── 📂 screenshots/                  # Test screenshots
│
├── 📂 target/                           # Maven build output
│   └── 📂 cucumber-reports/             # Cucumber reports
│
├── pom.xml                              # Maven dependencies
├── .gitignore                           # Git ignore rules
└── README.md                            # Project documentation
```

---

## ⚙️ Configuration

### Browser Configuration
Edit `src/test/resources/config/config.properties`:
```properties
# Browser options: chrome, firefox, edge
browser=chrome

# Timeouts (seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30
```

### Cucumber Options
Edit `CucumberTestRunner.java`:
```java
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.parabank.stepdefinitions"},
    tags = "${cucumber.filter.tags:@Smoke or @Regression}",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-html-report.html",
        "json:target/cucumber-reports/cucumber.json"
    }
)
```

### Logging Configuration
Edit `src/test/resources/config/log4j2.xml` to customize log levels and output.

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is for educational and testing purposes.

---

## 📧 Contact

For questions or suggestions, please open an issue in the repository.

---

## 🙏 Acknowledgments

- [ParaBank](https://parabank.parasoft.com/) - Demo banking application by Parasoft
- [Selenium](https://www.selenium.dev/) - Browser automation framework
- [Cucumber](https://cucumber.io/) - BDD framework
- [Extent Reports](https://www.extentreports.com/) - Reporting library
