package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import utilities.DriverManager;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions","hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json"
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {

        @AfterSuite
        public void tearDownSuite() {
                DriverManager.quitDriver();
        }
}