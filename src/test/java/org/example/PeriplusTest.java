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
    private String EMAIL = Config.EMAIL;
    private String PASSWORD = Config.PASSWORD;
    private String URL = Config.URL;
    private String PRODUCT_URL = Config.PRODUCT_URL;
    private String CART_URL = Config.CART_URL;
    private String ACCOUNT_URL = Config.ACCOUNT_URL;
    private String LOGIN_REDIRECT_URL = Config.LOGIN_REDIRECT_URL;

    @BeforeTest
    public void Setup() {
        cartItems = new HashMap<>();
        options = new ChromeOptions();
        driver = new ChromeDriver(options);

        driver.get(URL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }


    @Test(priority = 1)
    public void Login(){
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.navigateToLoginPage();

        loginPage.login(EMAIL, PASSWORD);

        try {
            boolean redirected = wait.until(ExpectedConditions.urlToBe(LOGIN_REDIRECT_URL));
        } catch (Exception e){
            Assert.fail("Login failed or not redirected correctly.");
        }
    }

    @Test(dependsOnMethods = {"Login"}, priority = 2)
    public void AddItemToCart(){
        HomePage homePage = new HomePage(driver, wait);
        homePage.navigateToHomePage();

        // ITEM IN CART COUNT
        Integer itemCount = homePage.getCartCount();

        // FIND A SINGLE PRODUCT
        WebElement singleProduct = homePage.getProduct();
        String productId = homePage.getProductId(singleProduct);

        // ADD ITEM TO CART
        cartItems.put(productId, 1);
        homePage.AddToCart(singleProduct);

        if(!homePage.compareCartCount(itemCount)){
            Reporter.log("Warning: Item count displayed is not increased.");
        }

        // VERIFY CART ITEMS
        CartPage cartPage = new CartPage(driver, wait);
        cartPage.navigateToCartPage();
        HashMap cartItemsSerialized = cartPage.getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @Test(dependsOnMethods = {"Login"}, priority = 3)
    public void AddMultipleItemsToCart() {
        HomePage homePage = new HomePage(driver, wait);
        homePage.navigateToHomePage();

        // FIND A MULTIPLE PRODUCT
        List<WebElement> products = homePage.getProducts(3);

        for (WebElement singleProduct : products) {
            Integer itemCount = homePage.getCartCount();

            String productId = homePage.getProductId(singleProduct);

            // ADD ITEM TO CART
            cartItems.put(productId, 1);
            homePage.AddToCart(singleProduct);

            if(!homePage.compareCartCount(itemCount)){
                Reporter.log("Warning: Item count displayed is not increased.");
            }
        }

        // VERIFY CART ITEMS
        CartPage cartPage = new CartPage(driver, wait);
        cartPage.navigateToCartPage();
        HashMap cartItemsSerialized = cartPage.getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @Test(dependsOnMethods = {"Login"}, priority = 4)
    public void AddQuantityItemsToCart() {
        HomePage homePage = new HomePage(driver, wait);
        homePage.navigateToHomePage();


        // FIND A SINGLE PRODUCT
        WebElement singleProduct = homePage.getProduct();
        String productId = homePage.getProductId(singleProduct);

        // GO TO PRODUCT PAGE
        ProductPage productPage = new ProductPage(driver, wait, productId);
        productPage.navigateToProductPage();

        Integer itemCount = productPage.getCartCount();

        // SET ITEM QUANTITY
        Random random = new Random();
        int randomQty = random.nextInt(3) + 1;
        productPage.setItemQty(randomQty);

        // ADD ITEM TO CART
        cartItems.put(productId, randomQty);
        productPage.addToCart();

        if(!productPage.compareCartCount(itemCount)){
            Reporter.log("Warning: Item count displayed is not increased.");
        }

        // VERIFY CART ITEMS
        CartPage cartPage = new CartPage(driver, wait);
        cartPage.navigateToCartPage();
        HashMap cartItemsSerialized = cartPage.getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @AfterTest
    public void cleanUp(){
        CartPage cartPage = new CartPage(driver, wait);
        if(driver.getCurrentUrl() != CART_URL){
            cartPage.navigateToCartPage();
        }

        cartPage.removeAllItems();
        driver.quit();
    }
}
