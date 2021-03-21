package org.levshunov.automated_testing.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class WebDriverUtils {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            return driver;
        }
        else {
            return driver;
        }
    }

    @Attachment(value = "{1}", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver, String name) {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{1}", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver, WebElement element) {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void findClick(WebDriver driver, String xpath) {
        driver.findElement(By.xpath(xpath)).click();
    }

    public static void findClick(WebDriver driver, String xpath, String screenshotName) {
        findClick(driver, xpath);
        takeScreenshot(driver, screenshotName);
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public enum ScreenshotClick {
        BEFORE, AFTER, NONE
    }

    public static void findScrollClick(WebDriver driver, String xpath, ScreenshotClick screenshot) {
        findScrollClick(driver, xpath, screenshot, null);
    }

    public static void findScrollClick(WebDriver driver, String xpath,
                                       ScreenshotClick screenshot, String screenshotName) {
        if (!ScreenshotClick.NONE.equals(screenshot) && StringUtils.isEmpty(screenshotName)) {
            Assert.fail("Incorrect screenshot name");
        }
        WebElement element;
        element = driver.findElement(By.xpath(xpath));
        scrollToElement(driver, element);
        if (ScreenshotClick.BEFORE.equals(screenshot)) {
            takeScreenshot(driver, screenshotName);
        }
        element.click();
        if (ScreenshotClick.AFTER.equals(screenshot)) {
            takeScreenshot(driver, screenshotName);
        }
    }
}

