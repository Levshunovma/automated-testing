package org.levshunov.automated_testing.ekatalog;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.levshunov.automated_testing.ekatalog.TestParts.openMainPage;
import static org.levshunov.automated_testing.utils.WebDriverUtils.*;

public class MaxPriceTest {

    private static final String XPATH_COMPUTERS = "//a[@href='/k169.htm']";
    private static final String XPATH_TABLETS = "//a[@href='/k169.htm']/../div/div/a[@href='/k30.htm']";
    private static final String XPATH_MAX_PRICE = "//input[@id='maxPrice_']";
    private static final String XPATH_SUBMIT = "//input[@id='match_submit']";
    private static final String XPATH_PRODUCT_LIST = "//form[@action='m1_compare.php']/div[not(@class)]";
    private static final String XPATH_PRODUCT_LIST_NEXT_PAGE = "//div[@class='list-more']//em";
    private static final String XPATH_PRODUCT_LIST_ONE_PRICE = "//form[@id='list_form1']/div//div[@class='pr31 ib']/span";
    private static final String XPATH_PRODUCT_LIST_MULTIPLE_PRICES = "//form[@id='list_form1']/div//div[@class='model-price-range']//span[1]";

    private void checkPrice(WebDriver driver, List<WebElement> elements, int maxPrice) {
        for (WebElement element : elements) {
            int price = Integer.parseInt(element.getText().replaceAll("[^\\d]", ""));
            if (price > maxPrice) {
                scrollToElement(driver, element);
                takeScreenshot(driver, "Ошибка! Цена больше указанной");
                Assert.fail("Price is more than maxPrice = " + maxPrice);
            }
        }
    }

    @Test
    @Parameters(value = "maxPrice")
    public void maxPriceTest(int maxPrice) {
        WebDriver driver =  getDriver();
        openMainPage(driver);

        findClick(driver, XPATH_COMPUTERS, "Открываем вкладку компьютеров");
        findClick(driver, XPATH_TABLETS, "Планшеты");

        driver.findElement(By.xpath(XPATH_MAX_PRICE)).sendKeys(String.valueOf(maxPrice));
        takeScreenshot(driver, "Устанавливаем максимальную цену");
        findScrollClick(driver, XPATH_SUBMIT, ScreenshotClick.BEFORE, "Нажимаем \"Подобрать\"");
        takeScreenshot(driver, "Получаем все подходящие товары");

        int pageNumber = 1;
        while (true) {
            try {
                WebElement nextPage = driver.findElement(By.xpath(XPATH_PRODUCT_LIST_NEXT_PAGE));
                nextPage.click();
                takeScreenshot(driver, "Листаем вниз для отображения всех товаров, конец страницы №" + pageNumber++);
                new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(nextPage));
            } catch (NoSuchElementException ex) {
                break;
            }
        }

        Assert.assertFalse(driver.findElements(By.xpath(XPATH_PRODUCT_LIST)).isEmpty(), "There are no products");
        checkPrice(driver, driver.findElements(By.xpath(XPATH_PRODUCT_LIST_ONE_PRICE)), maxPrice);
        checkPrice(driver, driver.findElements(By.xpath(XPATH_PRODUCT_LIST_MULTIPLE_PRICES)), maxPrice);
    }
}
