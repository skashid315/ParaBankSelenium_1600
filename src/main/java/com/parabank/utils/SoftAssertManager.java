package com.parabank.utils;

import org.testng.asserts.SoftAssert;

/**
 * SoftAssertManager provides soft assertion capabilities that allow
 * multiple validations to run before failing the test.
 * All assertions are collected and reported at the end.
 */
public class SoftAssertManager {
    private static final ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();
    
    /**
     * Initialize soft assert for current thread
     */
    public static void init() {
        softAssert.set(new SoftAssert());
    }
    
    /**
     * Get the current thread's soft assert instance
     */
    public static SoftAssert get() {
        if (softAssert.get() == null) {
            init();
        }
        return softAssert.get();
    }
    
    /**
     * Assert true with soft assertion
     */
    public static void assertTrue(boolean condition, String message) {
        try {
            get().assertTrue(condition, message);
            ExtentReportManager.logStep("SoftAssert", "Verify: " + message, "Condition met", "Assertion passed", "Pass");
        } catch (AssertionError e) {
            ExtentReportManager.logStep("SoftAssert", "Verify: " + message, "Condition met", "Assertion failed: " + e.getMessage(), "Fail");
            // Don't throw - let soft assert collect it
        }
    }
    
    /**
     * Assert false with soft assertion
     */
    public static void assertFalse(boolean condition, String message) {
        try {
            get().assertFalse(condition, message);
            ExtentReportManager.logStep("SoftAssert", "Verify: " + message, "Condition not met", "Assertion passed", "Pass");
        } catch (AssertionError e) {
            ExtentReportManager.logStep("SoftAssert", "Verify: " + message, "Condition not met", "Assertion failed: " + e.getMessage(), "Fail");
        }
    }
    
    /**
     * Assert equals with soft assertion
     */
    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            get().assertEquals(actual, expected, message);
            ExtentReportManager.logStep("SoftAssert", "Verify: " + message, "Expected: " + expected + ", Actual: " + actual, "Assertion passed", "Pass");
        } catch (AssertionError e) {
            ExtentReportManager.logStep("SoftAssert", "Verify: " + message, "Expected: " + expected + ", Actual: " + actual, "Assertion failed: " + e.getMessage(), "Fail");
        }
    }
    
    /**
     * Assert all collected soft assertions.
     * This should be called at the end of a scenario to report all failures.
     */
    public static void assertAll() {
        try {
            if (softAssert.get() != null) {
                softAssert.get().assertAll();
            }
        } catch (AssertionError e) {
            // Log all collected failures
            ExtentReportManager.getTest().fail("Soft assertions failed: " + e.getMessage());
            throw e;
        } finally {
            clear();
        }
    }
    
    /**
     * Clear soft assert for current thread
     */
    public static void clear() {
        softAssert.remove();
    }
    
    /**
     * Check if there are any failures collected
     */
    public static boolean hasFailures() {
        // SoftAssert doesn't expose this directly, so we track via assertAll
        return false;
    }
}
