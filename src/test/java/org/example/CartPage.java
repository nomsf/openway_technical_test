package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private String URL = Config.CART_URL;

    // Locators
    private By cartItems = By.cssSelector(".row.row-cart-product");
    private By removeButton = By.xpath("./div[2]/div[5]/div/a[1]");
    private By productID = By.xpath("./div[2]/div[2]");
    private By qty = By.xpath("./div[2]/div[4]/div/input");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateToCartPage() {
        driver.get(URL);
    }

    public HashMap<String, Integer> getCartItems(){

        // WAIT FOR CART ITEMS TO LOAD
        try{
            Thread.sleep(2000);
        } catch (Exception e){
            Assert.fail("Error when waiting for the cart items to load.");
        }

        List<WebElement> itemsElements = driver.findElements(cartItems);

        // SERIALIZE CART ITEMS
        HashMap<String, Integer> cartItemsSerialized = new HashMap<>();
        for (WebElement itemElement : itemsElements){
            String productId = itemElement.findElement(productID).getText().trim();
            int quantity = Integer.parseInt(itemElement.findElement(qty).getAttribute("value"));
            cartItemsSerialized.put(productId, quantity);
        }
        Reporter.log("\n\nCart Items: " + cartItems);
        Reporter.log("\n\nCart Items Serialized: " + cartItemsSerialized);

        return cartItemsSerialized;
    }

    public void removeAllItems() {
        while (true){
            try{
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
                List<WebElement> itemsElements = driver.findElements(cartItems);
                itemsElements.get(0).findElement(removeButton).click();
            } catch (Exception e){
                break;
            }
        }
    }



}
