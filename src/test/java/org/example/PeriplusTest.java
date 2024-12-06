package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class PeriplusTest
{
    public static WebDriver driver;
    public static ChromeOptions options;
    public static WebDriverWait wait;

    private String EMAIL = "naufalindev@gmail.com";
    private String PASSWORD = "periplus@123";
    private String productName;

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

//      Input Emaill
        driver.findElement(By.xpath("//*[@id=\"login\"]/div/table/tbody/tr[2]/td/input")).sendKeys(EMAIL);
//      Input Password
        driver.findElement(By.xpath("//*[@id=\"ps\"]")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//*[@id=\"button-login\"]")).click();

    }

    @Test(dependsOnMethods = {"Login"})
    public void AddItemToCart(){
        driver.findElement(By.xpath("/html/body/header/div[2]/div/div[1]/div[1]/div[1]/a")).click();
        WebElement singleProduct = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]/div/div[2]/div/div[1]/div[1]/div/div[4]/div"));

        productName = singleProduct.findElement(By.className("product-content product-contents")).findElement(By.xpath(".//h3/a")).getText();

        System.out.println(productName);

        singleProduct.findElement(By.className("product-action")).findElement(By.xpath(".//a[1]")).click();

    }
}
