package utilities;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class Scrollheighticreased implements ExpectedCondition<Boolean>{
    private final long previousHeight;
    private final WebDriver driver;

    public Scrollheighticreased(long previousHeight,WebDriver driver) {
        this.previousHeight = previousHeight;
        this.driver = driver;
    }

    @Override
    public Boolean apply(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) this.driver;
        long currentHeight = (Long) jsExecutor.executeScript("return document.body.scrollHeight");
        return currentHeight > previousHeight;
    }

}

