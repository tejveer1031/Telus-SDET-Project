package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScrollerHandler {
    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    public ScrollerHandler(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement findElementWithSubtitle(String subtitle) {
        JavascriptExecutor js = (JavascriptExecutor) driver; // Cast driver to javascriptExecutor
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight"); // fetch current height of document body
        while (true) { //infinite loop
            List<WebElement> items = driver.findElements(By.xpath("//div[@class='item-container']"));
            for (WebElement item : items) {
                try {
                    WebElement subtitleElement = item.findElement(By.xpath(".//p[@class='subtitle']")); // we fetch subtitle from each div
                    String subtitleText = subtitleElement.getText().trim();
                    subtitleText = Normalizer.normalize(subtitleText, Normalizer.Form.NFKC); // convert char to standard form

                    if (subtitleText.equalsIgnoreCase(subtitle) || subtitleText.contains(subtitle)) {
                        return item;
                    }
                } catch (NoSuchElementException e) {
                    continue;
                }
            }

            js.executeScript("window.scrollTo(0, document.body.scrollHeight);"); // scroll to bottom of page

//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


            try {
                wait.until(new Scrollheighticreased(lastHeight, this.driver));
                lastHeight = (long) js.executeScript("return document.body.scrollHeight");
            } catch (TimeoutException e) {
                long currentHeight = (long) js.executeScript("return document.body.scrollHeight"); // Fresh check
                if (currentHeight == lastHeight) {
                    break;
                }
                lastHeight = currentHeight; // Update if partial load
            }

//            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
//            if (newHeight == lastHeight) {
//                break;
//            }
//            lastHeight = newHeight;
        }
        return null;
    }

}

