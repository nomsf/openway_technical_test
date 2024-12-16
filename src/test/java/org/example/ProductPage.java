package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private String URL = Config.PRODUCT_URL;

    // Locators
    private By itemQtyInputField = By.xpath("/html/body/div[3]/div[1]/div/div/div[3]/div[1]/div[2]/input");
    private By addToCartButton = By.xpath("/html/body/div[3]/div[1]/div/div/div[3]/div[1]/div[3]/div[1]/button");
    private By cartCount = By.id("cart_total");


    public ProductPage(WebDriver driver, WebDriverWait wait, String productId) {
        this.driver = driver;
        this.wait = wait;
        this.URL += productId;
    }

    public void navigateToProductPage() {
        driver.get(URL);
    }

    public Integer getCartCount() {
        return Integer.valueOf(driver.findElement(cartCount).getText());
    }

    public void setItemQty(int qty) {
        driver.findElement(itemQtyInputField).sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(qty));
    }

    public void addToCart() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        driver.findElement(addToCartButton).click();
    }

    public boolean compareCartCount(Integer itemCount) {
        try {
            wait.until(booleanExpectedCondition -> {
                Integer itemCountNew = getCartCount();
                return itemCountNew > itemCount;
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}