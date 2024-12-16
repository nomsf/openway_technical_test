package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private String LOGIN_URL = "https://www.periplus.com/account/Login";

    // Locators
    private By emailField = By.xpath("//*[@id=\"login\"]/div/table/tbody/tr[2]/td/input");
    private By passwordField = By.xpath("//*[@id=\"ps\"]");
    private By loginButton = By.xpath("//*[@id=\"button-login\"]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateToLoginPage(){
        driver.get(LOGIN_URL);
    }

    public void login(String email, String password) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

}
