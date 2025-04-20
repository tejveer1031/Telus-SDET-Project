package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;
import java.util.List;

public class ScrollerHandler {
    final JavascriptExecutor js; // Cast driver to javascriptExecutor
    final By ITEM_CONTAINER = By.xpath("//div[@class='item-container']");
    final By SUBTITLE_LOCATOR = By.xpath(".//p[@class='subtitle']");
    WebDriver driver;
    WebDriverWait wait;


    public ScrollerHandler(WebDriver driver) {
       this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    public WebElement findElementWithSubtitle(String subtitle) {

        // Preprocess search text once
        final String normalizedSubtitle = Normalizer.normalize(subtitle.trim(), Normalizer.Form.NFKC).toLowerCase();

        long lastHeight = findCurrentHeightOfWindow();


        while (true) { //infinite loop


            // Check current batch of items
            for (WebElement item : driver.findElements(ITEM_CONTAINER)) {
                final List<WebElement> subtitleElements = item.findElements(SUBTITLE_LOCATOR);

                if (subtitleElements.isEmpty()) continue;
                // Process element text
                final String elementText = Normalizer.normalize(
                        subtitleElements.get(0).getText().trim(),
                        Normalizer.Form.NFKC
                ).toLowerCase();

                if (elementText.contains(normalizedSubtitle)) {
                    return item;
                }
            }
            scroolToBottom();
            try {
                wait.until(new Scrollheighticreased(lastHeight, this.driver));
                lastHeight = findCurrentHeightOfWindow();
            } catch (TimeoutException e) {
                final long currentHeight = findCurrentHeightOfWindow();
                if (currentHeight == lastHeight) break; // No more content
                lastHeight = currentHeight;
            }
        }
        return null;
    }

    private Long findCurrentHeightOfWindow() {
        return (long) js.executeScript("return document.body.scrollHeight");
    }

    private void scroolToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);"); // scroll to bottom of page
    }


}

