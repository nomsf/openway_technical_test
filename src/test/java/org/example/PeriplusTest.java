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
    private String LOGIN_REDIRECT_URL = "https://www.periplus.com/_index_/index";

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
        HashMap cartItemsSerialized = getCartItems();
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
        HashMap cartItemsSerialized = getCartItems();
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
        HashMap cartItemsSerialized = getCartItems();
        Assert.assertEquals(cartItemsSerialized, cartItems, "Cart items do not match.");
    }

    @AfterTest
    public void cleanUp(){
        removeAllItemFromCart();
        driver.quit();
    }
}
