package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PeriplusTest
{
    public static WebDriver driver;
    public static ChromeOptions options;
    public static WebDriverWait wait;
    private HashMap<String, Integer> cartItems;

    // CONSTANTS VALUES
    private String EMAIL = "naufalindev@gmail.com";
    private String PASSWORD = "periplus@123";
    private String URL = "https://www.periplus.com/";
    private String PRODUCT_URL = "https://www.periplus.com/p/";
    private String CART_URL = "https://www.periplus.com/checkout/cart";
    private String ACCOUNT_URL = "https://www.periplus.com/account/Your-Account";

    @BeforeTest
    public void Setup() {
        cartItems = new HashMap<>();
        options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        driver.get(URL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    // UTILITIES METHODS
    public void navigateToHomePage(){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/header/div[2]/div/div[1]/div[1]/div[1]")));
        homeButton.click();
    }

    public HashMap<String, Integer> getCartItems(){

        driver.get(CART_URL);

        // WAIT FOR CART ITEMS TO LOAD
        try{
            Thread.sleep(5000);
        } catch (Exception e){
            Assert.fail("Error when waiting for the cart items to load.");
        }

        List<WebElement> itemsElements = driver.findElements(By.cssSelector(".row.row-cart-product"));

        // SERIALIZE CART ITEMS
        HashMap<String, Integer> cartItemsSerialized = new HashMap<>();
        for (WebElement itemElement : itemsElements){
            Reporter.log("\n\nItem Element: " + itemElement.getAttribute("outerHTML"));
            String productId = itemElement.findElement(By.xpath("./div[2]/div[2]")).getText().trim();
            int quantity = Integer.parseInt(itemElement.findElement(By.xpath("./div[2]/div[4]/div/input")).getAttribute("value"));
            cartItemsSerialized.put(productId, quantity);
        }
        Reporter.log("\n\nCart Items: " + cartItems);
        Reporter.log("\n\nCart Items Serialized: " + cartItemsSerialized);

        return cartItemsSerialized;
    }

    public void removeAllItemFromCart(){
        if(driver.getCurrentUrl() != CART_URL){
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
            WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"show-your-cart\"]/a")));
            cartButton.click();
        }

        while (true){
            try{
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
                List<WebElement> itemsElements = driver.findElements(By.cssSelector(".row.row-cart-product"));
                itemsElements.get(0).findElement(By.xpath("./div[2]/div[5]/div/a[1]")).click();
            }catch (Exception e){
                break;
            }
        }
    }

    private static boolean isElementPresent(WebDriver driver, WebDriverWait wait, By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true; // Element is found
        } catch (Exception e) {
            return false; // Element not found
        }
    }

    @Test(priority = 1)
    public void Login(){
        driver.findElement(By.xpath("/html/body/header/div[2]/div/div[1]/div[3]/div/div[3]")).click();

//      Input Email
        driver.findElement(By.xpath("//*[@id=\"login\"]/div/table/tbody/tr[2]/td/input")).sendKeys(EMAIL);
//      Input Password
        driver.findElement(By.xpath("//*[@id=\"ps\"]")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//*[@id=\"button-login\"]")).click();

        // WAIT FOR LOGIN TO COMPLETE
        try {
            boolean redirected = wait.until(ExpectedConditions.urlToBe(ACCOUNT_URL));

            Assert.assertTrue(redirected, "Login failed or not redirected correctly.");
        } catch (Exception e){
            Assert.fail("Login failed or not redirected correctly.");
        }
    }

    @Test(dependsOnMethods = {"Login"}, priority = 2)
    public void AddItemToCart(){
        navigateToHomePage();
        // ITEM IN CART COUNT
        Integer itemCount = Integer.valueOf(driver.findElement(By.id("cart_total")).getText());

        // FIND A SINGLE PRODUCT
        //        driver.findElement(By.xpath("/html/body/header/div[2]/div/div[1]/div[1]/div[1]/a")).click();
        WebElement singleProduct = driver.findElement(By.className("single-product"));

//        Reporter.log(singleProduct.getAttribute("outerHTML"));


        // Get the href attribute value
        WebElement linkElement = singleProduct.findElement(By.tagName("a"));
        String hrefValue = linkElement.getAttribute("href");


        // EXTRACT ID FROM HREF VALUE
        int startIndex = hrefValue.indexOf("/p/") + 3;
        int endIndex = hrefValue.indexOf("/", startIndex);
        String extractedValue = hrefValue.substring(startIndex, endIndex);

        cartItems.put(extractedValue, 1);

//        Reporter.log("\n\nProduct Number ID: " + extractedValue);

        // ADD ITEM TO CART
        WebElement addToCartButton = singleProduct.findElement(By.className("addtocart"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", addToCartButton);

        try{
            wait.until(booleanExpectedCondition -> {
                Integer itemCountNew = Integer.valueOf(driver.findElement(By.id("cart_total")).getText());
                return itemCountNew == itemCount+1;
            });
        } catch(Exception e){
            Reporter.log("\n\nError: " + e.getMessage());
            Reporter.log("\nItem Count: " + itemCount);
            Reporter.log("\nItem Count New: " + driver.findElement(By.id("cart_total")).getText());

        }

        // VERIFY CART ITEMS
        HashMap cartItemsSerialized = getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @Test(dependsOnMethods = {"Login"}, priority = 3)
    public void AddMultipleItemsToCart() {
        navigateToHomePage();

        // FIND A SINGLE PRODUCT
        //        driver.findElement(By.xpath("/html/body/header/div[2]/div/div[1]/div[1]/div[1]/a")).click();
        List<WebElement> products = driver.findElements(By.className("single-product"));
        List<WebElement> productsLimited = products.subList(0, Math.min(5, products.size()));

        for (WebElement singleProduct : productsLimited) {
            Integer itemCount = Integer.valueOf(driver.findElement(By.id("cart_total")).getText());

            // Get the href attribute value
            WebElement linkElement = singleProduct.findElement(By.tagName("a"));
            String hrefValue = linkElement.getAttribute("href");

            // EXTRACT ID FROM HREF VALUE
            int startIndex = hrefValue.indexOf("/p/") + 3;
            int endIndex = hrefValue.indexOf("/", startIndex);
            String extractedValue = hrefValue.substring(startIndex, endIndex);

            cartItems.put(extractedValue, 1);

            // ADD ITEM TO CART
            WebElement addToCartButton = singleProduct.findElement(By.className("addtocart"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", addToCartButton);

            try{
                wait.until(booleanExpectedCondition -> {
                    Integer itemCountNew = Integer.valueOf(driver.findElement(By.id("cart_total")).getText());
                    return itemCountNew > itemCount;
                });

            } catch (Exception e){
                Reporter.log("\n\nError: " + e.getMessage());
                Reporter.log("\nItem Count: " + itemCount);
                Reporter.log("\nItem Count New: " + driver.findElement(By.id("cart_total")).getText());
            }

        }

        // VERIFY CART ITEMS
        HashMap cartItemsSerialized = getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @Test(dependsOnMethods = {"Login"}, priority = 4)
    public void AddQuantityItemsToCart() {
        navigateToHomePage();
        Random random = new Random();
        int randomQty = random.nextInt(3) + 1;

        // FIND A SINGLE PRODUCT
        WebElement singleProduct = driver.findElement(By.className("single-product"));

        // Get the href attribute value
        WebElement linkElement = singleProduct.findElement(By.tagName("a"));
        String hrefValue = linkElement.getAttribute("href");

        // EXTRACT ID FROM HREF VALUE
        int startIndex = hrefValue.indexOf("/p/") + 3;
        int endIndex = hrefValue.indexOf("/", startIndex);
        String extractedValue = hrefValue.substring(startIndex, endIndex);

        cartItems.put(extractedValue, randomQty);

        // ADD ITEM TO CART
        Integer itemCount = Integer.valueOf(driver.findElement(By.id("cart_total")).getText());
        driver.get(PRODUCT_URL + extractedValue);

        WebElement ItemQty = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/div[3]/div[1]/div[2]/input"));
//        ItemQty.clear();
        Reporter.log("\n\nRandom Quantity: " + randomQty);
        ItemQty.sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(randomQty));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        WebElement addToCartButton = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/div[3]/div[1]/div[3]/div[1]/button"));
        addToCartButton.click();

        try {
            wait.until(booleanExpectedCondition -> {
                Integer itemCountNew = Integer.valueOf(driver.findElement(By.id("cart_total")).getText());
                return itemCountNew > itemCount;
            });
        } catch (Exception e){
            Reporter.log("\n\nError: " + e.getMessage());
            Reporter.log("\nItem Count: " + itemCount);
            Reporter.log("\nItem Count New: " + driver.findElement(By.id("cart_total")).getText());
        }

        // VERIFY CART ITEMS
        HashMap cartItemsSerialized = getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @AfterTest
    public void cleanUp(){
        removeAllItemFromCart();
        driver.quit();
    }
}
