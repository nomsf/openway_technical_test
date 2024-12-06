package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class EmailTest {
    public static WebDriver driver;
    public static ChromeOptions options;
    public static WebDriverWait wait;

    private String EMAIL = "naufalindev@gmail.com";
    private String PASSWORD = "319566@aiL";
    private String productName;

    @BeforeTest
    public void Setup() {

        options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://mail.google.com/mail/");
    }

    @Test
    public void Login(){


        driver.findElement(By.xpath("//*[@id=\"identifierId\"]")).sendKeys(EMAIL);
        driver.findElement(By.xpath("//*[@id=\"identifierNext\"]/div/button")).click();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input")));
        passwordField.sendKeys(PASSWORD);
//        driver.findElement(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//*[@id=\"passwordNext\"]/div/button")).click();
    }

//    @Test(dependsOnMethods = {"Login"})
//    public void DeleteUnreadEmailHover(){
//        WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"gs_lc50\"]/input[1]")));
//        searchBar.sendKeys("is:unread");
////        driver.findElement(By.xpath("//*[@id=\"gs_lc50\"]/input[1]")).sendKeys("is:unread");
//        driver.findElement(By.xpath("//*[@id=\"aso_search_form_anchor\"]/button[4]")).click();
//
//        WebElement emails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='F cf zt']/tbody")));
////        WebElement emails = driver.findElement(By.xpath("//*[@id=\":21\"]/tbody"));
////
//        // Delete First Unread Email
//        Actions actions = new Actions(driver);
//        WebElement email = wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(emails, By.xpath(".//tr"))).get(0);
////        WebElement email = emails.findElement(By.xpath(".//tr"));
//        String emailSubject = email.findElement(By.className("bqe")).getText();
//        actions.moveToElement(email).perform();
//        email.findElement(By.className("bqX bru")).click();
//
//        // Check if email still there
//        email = emails.findElement(By.xpath(".//tr"));
//        String emailSubjectNew = email.findElement(By.className("bqe")).getText();
//        Assert.assertNotEquals(emailSubjectNew, emailSubject, "The email is not deleted");
//    }

    @Test(dependsOnMethods = {"Login"})
    public void DeleteAllUnreadEmailSelect(){
        WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"gs_lc50\"]/input[1]")));
        searchBar.sendKeys("is:unread");
        driver.findElement(By.xpath("//*[@id=\"aso_search_form_anchor\"]/button[4]")).click();

        WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":5n\"]/div[1]/span")));
        checkbox.click();

        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@aria-label=\"Delete\"]")));
        deleteButton.click();

    }
}
