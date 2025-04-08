package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScrollerHandler {
    WebDriver driver;
    public ScrollerHandler(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement findElementWithSubtitle(String subtitle) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            List<WebElement> items = driver.findElements(By.xpath("//div[@class='item-container']"));
            for (WebElement item : items) {
                try {
                    WebElement subtitleElement = item.findElement(By.xpath(".//p[@class='subtitle']"));
                    String subtitleText = subtitleElement.getText().trim();
                    subtitleText = Normalizer.normalize(subtitleText, Normalizer.Form.NFKC);



                    if (subtitleText.equalsIgnoreCase(subtitle)) {
                        return item;
                    }

                    if(subtitleText.contains(subtitle)){
                        return item;
                    }

                } catch (NoSuchElementException e) {
                    continue;
                }
            }

            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) {
                break;
            }
            lastHeight = newHeight;
        }
        return null;
    }

}

