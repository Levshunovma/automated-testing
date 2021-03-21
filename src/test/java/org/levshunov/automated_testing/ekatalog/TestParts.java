package org.levshunov.automated_testing.ekatalog;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.levshunov.automated_testing.utils.WebDriverUtils.findClick;
import static org.levshunov.automated_testing.utils.WebDriverUtils.takeScreenshot;

public class TestParts {

    private static final String URL = "https://www.e-katalog.ru";

    public static final String LOGIN = "You should add your own login here";
    private static final String PASSWORD = "You should add your own password here";

    private static final String XPATH_LOGIN_BUTTON = "//*[@class='wu_entr']";
    private static final String XPATH_LOGIN_EMAIL_BUTTON = "//div[contains(@class, 'signin-with-ek')]";
    private static final String XPATH_LOGIN_NAME_FIELD = "//*[@name='l_']";
    private static final String XPATH_LOGIN_PASSWORD_FIELD = "pw_";
    private static final String XPATH_LOGIN_SUBMIT_BUTTON = "//div[contains(@class, 'signin-actions')]/button[@type='submit']";
    private static final String XPATH_LOGOUT_BUTTON = "//a[@class='help2']";

    public static void openMainPage(WebDriver driver) {
        driver.manage().window().maximize();
        driver.get(URL);
    }

    public static void login(WebDriver driver) {
        login(driver, false);
    }

    public static void login(WebDriver driver, boolean makeScreenshots) {
        findClick(driver, XPATH_LOGIN_BUTTON);
        findClick(driver, XPATH_LOGIN_EMAIL_BUTTON);
        if (makeScreenshots) {
            takeScreenshot(driver, "Окно входа");
        }

        driver.findElement(By.xpath(XPATH_LOGIN_NAME_FIELD)).sendKeys(LOGIN);
        driver.findElement(By.name(XPATH_LOGIN_PASSWORD_FIELD)).sendKeys(PASSWORD);
        if (makeScreenshots) {
            takeScreenshot(driver, "Ввод логина и пароля");
        }
        findClick(driver, XPATH_LOGIN_SUBMIT_BUTTON);
        if (makeScreenshots) {
            takeScreenshot(driver, "Вход произведен");
        }
    }

    public static void logout(WebDriver driver) {
        logout(driver, false);
    }

    public static void logout(WebDriver driver, boolean makeScreenshots) {
        findClick(driver, XPATH_LOGOUT_BUTTON);
        if (makeScreenshots) {
            takeScreenshot(driver, "Выход произведен");
        }
    }
}
