package com.parabank.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import com.parabank.utils.ExtentReportManager;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.parabank.stepdefinitions"},
    tags = "${cucumber.filter.tags:@Smoke or @Regression}",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-html-report.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public void beforeClass() {
        ExtentReportManager.getInstance();
    }

    @AfterClass
    public void afterClass() {
        ExtentReportManager.flushReport();
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
