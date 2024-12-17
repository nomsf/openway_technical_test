package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private String URL = Config.URL;

    // Locators
    private By cartCount = By.id("cart_total");
    private By product = By.className("single-product");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateToHomePage(){
        driver.get(URL);
    }

    public Integer getCartCount() {
        return Integer.valueOf(driver.findElement(cartCount).getText());
    }

    public WebElement getProduct() {
        return driver.findElement(product);
    }

    public List<WebElement> getProducts(int count) {
        List<WebElement> products = driver.findElements(product);
        return products.subList(0, count);
    }

    public String getProductId(WebElement product) {
        // Get the href attribute value
        WebElement linkElement = product.findElement(By.tagName("a"));
        String hrefValue = linkElement.getAttribute("href");

        // EXTRACT ID FROM HREF VALUE
        int startIndex = hrefValue.indexOf("/p/") + 3;
        int endIndex = hrefValue.indexOf("/", startIndex);

        return hrefValue.substring(startIndex, endIndex);
    }

    public List<String> getProductIds(List<WebElement> products) {
        List<String> productIds = new ArrayList<>();
        for (WebElement product : products) {
            productIds.add(getProductId(product));
        }
        return productIds;
    }

    public void AddToCart(WebElement product) {
        WebElement addToCartButton = product.findElement(By.className("addtocart"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", addToCartButton);

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
