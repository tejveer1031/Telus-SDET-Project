package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;
import utilities.DriverManager;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions","hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)

public class TestRunner extends AbstractTestNGCucumberTests {
        @BeforeClass(alwaysRun = true)
        @Parameters("browser")
        public void setup(String browser) {
                DriverManager.setBrowser(browser);// Thread-safe browser setup
                DriverManager.getDriver().manage().window().maximize();
        }

        @AfterClass
        public void teardown() {
                DriverManager.quitDriver();
        }
}