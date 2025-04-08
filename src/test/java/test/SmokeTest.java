package test;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ConfigReader;
import utilities.DriverManager;

public class SmokeTest {

    @BeforeMethod
    public void setup() {
        DriverManager.getDriver().manage().window().maximize();
    }

    @Test
    public void verifyPageLoad() {
        WebDriver driver = DriverManager.getDriver();
        String url = ConfigReader.getProperty("base.url");
        driver.get(url);

        // Option 1: Validate expected URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("telustvplus.com"),
                "Not on TELUS TV+ site. Actual URL: " + currentUrl);

    }

//    @AfterMethod
//    public void teardown() {
//        DriverManager.quitDriver();
//    }
}