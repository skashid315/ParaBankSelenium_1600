package com.parabank.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/test-output/screenshots/";

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            File directory = new File(SCREENSHOT_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileUtils.copyFile(source, destination);

            logger.info("Screenshot captured: " + filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
