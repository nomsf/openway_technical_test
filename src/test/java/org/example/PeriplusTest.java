package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;

public class PeriplusTest
{
    public static WebDriver driver;
    public static ChromeOptions options;
    public static WebDriverWait wait;

    private String EMAIL = "naufalindev@gmail.com";
    private String PASSWORD = "periplus@123";
    private HashMap<String, Integer> cartItems = new HashMap<>();

    @BeforeTest
    public void Setup() {

        options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        driver.get("https://www.periplus.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    @Test
    public void Login(){
        driver.findElement(By.xpath("/html/body/header/div[2]/div/div[1]/div[3]/div/div[3]")).click();

//      Input Email
        driver.findElement(By.xpath("//*[@id=\"login\"]/div/table/tbody/tr[2]/td/input")).sendKeys(EMAIL);
//      Input Password
        driver.findElement(By.xpath("//*[@id=\"ps\"]")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//*[@id=\"button-login\"]")).click();

    }

    @Test(dependsOnMethods = {"Login"})
    public void AddItemToCart(){

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/header/div[2]/div/div[1]/div[1]/div[1]")));
        homeButton.click();

        // FIND A SINGLE PRODUCT
        //        driver.findElement(By.xpath("/html/body/header/div[2]/div/div[1]/div[1]/div[1]/a")).click();
        WebElement singleProduct = driver.findElement(By.className("single-product"));

        Reporter.log(singleProduct.getAttribute("outerHTML"));


        // Get the href attribute value
        WebElement linkElement = singleProduct.findElement(By.tagName("a"));
        String hrefValue = linkElement.getAttribute("href");

////*[@id="content"]/div[2]/div/div[2]/div/div[1]/div[1]/div/div[7]/div/div[1]/a

        // EXTRACT ID FROM HREF VALUE
        int startIndex = hrefValue.indexOf("/p/") + 3;
        int endIndex = hrefValue.indexOf("/", startIndex);
        String extractedValue = hrefValue.substring(startIndex, endIndex);

        cartItems.put(extractedValue, 1);
        Reporter.log("\n\nProduct Number ID: " + extractedValue);

        // ADD ITEM TO CART
        WebElement addToCartButton = singleProduct.findElement(By.className("addtocart"));

        Reporter.log("\n\nAdd to Cart Button: " + addToCartButton.getAttribute("outerHTML"));

        String jsFunction = addToCartButton.getAttribute("onclick");
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(jsFunction);

    }
}
