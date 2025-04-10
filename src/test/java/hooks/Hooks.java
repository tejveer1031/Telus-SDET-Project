package hooks;

import ch.qos.logback.core.util.FileUtil;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utilities.DriverManager;
import java.io.File;
import java.io.IOError;
import java.io.IOException;



public class Hooks {


    @After
    public void tearDown(Scenario scenario) {
        try {
            Thread.sleep(2000); // 2000 milliseconds = 2 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
        }
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot_After_each_test");
    }


}
