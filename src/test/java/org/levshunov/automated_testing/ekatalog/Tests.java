package org.levshunov.automated_testing.ekatalog;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import static org.levshunov.automated_testing.ekatalog.TestParts.*;
import static org.levshunov.automated_testing.utils.WebDriverUtils.*;

public class Tests {

    private static final String PRODUCT_NAME = "Sony HDR-AZ1VB";
    private static final String XPATH_NAME_DISPLAY = "//*[@class='info-nick']";
    private static final String XPATH_GADGETS = "//a[@href='/k119.htm']";
    private static final String XPATH_ACTION_CAMERAS = "//a[@href='/k119.htm']/../div/div/a[@href='/k695.htm']";
    private static final String XPATH_SELECTION_SONY = "//label[@for='br156']";
    private static final String XPATH_SELECTION_NFC = "//label[@for='c17983']";
    private static final String XPATH_SELECTION_SUBMIT = "//input[@id='match_submit']";
    private static final String XPATH_PRODUCT = "//span[text()='" + PRODUCT_NAME + "']";
    private static final String XPATH_WISHLIST = "//div[@class='user-div']//a[@title='" + PRODUCT_NAME + "'][2]";
    private static final String XPATH_WISHLIST_ADD = "//span[contains(@class, 'addto-wishlist')]";
    private static final String XPATH_WISHLIST_DELETE_HOVER = XPATH_WISHLIST + "/..";
    private static final String XPATH_WISHLIST_DELETE_CLICK = XPATH_WISHLIST_DELETE_HOVER + "//div[@class='whishlist-action--remove']";
    private static final String XPATH_HISTORY = "//div[@class='user-history-div']//u[text()='" + PRODUCT_NAME + "']";
    private static final String XPATH_HISTORY_DELETE = XPATH_HISTORY + "/..//div[@class='user-history-close']";

    @Test
    public static void loginTest() {
        WebDriver driver = getDriver();
        openMainPage(driver);
        login(driver, true);
        Assert.assertEquals(driver.findElement(By.xpath(XPATH_NAME_DISPLAY)).getText(), LOGIN);
        logout(driver, true);
    }

    @Test(dependsOnMethods = {"loginTest"})
    public void findHistoryWishlistTest() {
        WebDriver driver = getDriver();
        openMainPage(driver);
        login(driver);

        new WebDriverWait(driver, 10)
            .until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(XPATH_GADGETS))));
        findClick(driver, XPATH_GADGETS, "Открываем вкладку гаджетов");
        findClick(driver, XPATH_ACTION_CAMERAS, "Action камеры");
        findScrollClick(driver, XPATH_SELECTION_SONY, ScreenshotClick.AFTER, "Выбираем камеры Sony");
        findScrollClick(driver, XPATH_SELECTION_NFC, ScreenshotClick.AFTER, "Выбираем камеры с NFC-чипом");
        findScrollClick(driver, XPATH_SELECTION_SUBMIT, ScreenshotClick.BEFORE, "Нажимаем \"Подобрать\"");
        takeScreenshot(driver, "Получаем все подходящие товары");

        WebElement product = driver.findElement(By.xpath(XPATH_PRODUCT));
        Assert.assertEquals(product.getText(), PRODUCT_NAME);
        scrollToElement(driver, product);
        takeScreenshot(driver, "Ищем камеру " + PRODUCT_NAME);
        product.click();
        findClick(driver, XPATH_WISHLIST_ADD, "Добавляем камеру в закладки");

        findClick(driver, XPATH_NAME_DISPLAY, "Открываем профиль");
        Assert.assertTrue(driver.findElement(By.xpath(XPATH_WISHLIST)).isEnabled());
        Assert.assertTrue(driver.findElement(By.xpath(XPATH_HISTORY)).isEnabled());
        findClick(driver, XPATH_HISTORY_DELETE, "Удаляем из просмотренного");
        new Actions(driver)
            .moveToElement(driver.findElement(By.xpath(XPATH_WISHLIST_DELETE_HOVER)))
            .build().perform();
        findClick(driver, XPATH_WISHLIST_DELETE_CLICK, "Удаляем из закладок");

        logout(driver, true);
    }

    @AfterSuite
    private void stopDriver() {
        getDriver().quit();
    }
}
