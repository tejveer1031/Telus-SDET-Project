package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ScrollerHandler;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


public class SelectanimatedPage {
    private final WebDriverWait wait;
    private final WebDriver driver;


    @FindBy(xpath = "//a[contains(.,'On Demand')]")
    private WebElement onDemand;

    @FindBy(xpath = "//a[contains(@class, 'selected')]")
    private WebElement onDemandSelected;

    @FindBy(xpath = "//div[@class='title']//a[@href='#/viewall/TRAY/SEARCH/VOD?filter_contentSubtype=movie&title=Movies']")
    private WebElement movies;

    @FindBy(css = "a[href*='/viewall/TRAY/SEARCH/VOD?filter_contentSubtype=movie&title=Movies'] button.imageButton")
    private WebElement viewAll;

    @FindBy(css = "img.filter-icon")
    private WebElement filterButton;

    @FindBy(xpath = "//div[@data='filter-list-items']//label[contains(@class, 'dropdown-items-container')]")
    private List<WebElement> filterItems;

    @FindBy(xpath = "//div[contains(@class, 'filter-button') and contains(@class, 'filter-apply-button') and contains(@class, 'filter-button-enabled') and text()='Apply']")
    private WebElement applyButton;

    @FindBy(xpath = "div.item-container")
    private List<WebElement> listOfAsset;

    public SelectanimatedPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(35));
        PageFactory.initElements(driver, this);
    }


    public void clickOnDemandMovies() {
        wait.until(ExpectedConditions.elementToBeClickable(onDemand)).click();
    }


    public boolean onDemandSelected() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(onDemand)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public void scrollToMovies() {
        handleStalement(movies, By.xpath("//div[@class='title']//a[@href='#/viewall/TRAY/SEARCH/VOD?filter_contentSubtype=movie&title=Movies']"));
    }

    public void handleStalement(WebElement element, By xpath) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", element);
                return;
            } catch (StaleElementReferenceException e) {
                movies = driver.findElement(xpath);
                scrollToMovies();
                attempts++;
            }
        }
        throw new RuntimeException("Failed to scroll to movies after " + attempts + " attempts");
    }

    public void clickToViewAll() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(viewAll));
        viewAll.click();

    }

    public boolean verifyUserMustBeOnMovies() {
        String expectedUrl = "https://telustvplus.com/#/viewall/TRAY/SEARCH/VOD?filter_contentSubtype=movie&title=Movies&orderBy=title&sortOrder=asc";
        String actualUrl = driver.getCurrentUrl();
        return expectedUrl.equals(actualUrl);
    }

    public void filterAnimatedMovies() {
        wait.until(ExpectedConditions.visibilityOf(filterButton)).click();
    }

    public void chooseAnimatedMovies() {
        for (WebElement item : filterItems) {
            String trimmed = item.getText().replaceAll("^'+|'+$", "");
            if (trimmed.equalsIgnoreCase("Animated")) {
                WebDriverWait waits = new WebDriverWait(driver, Duration.ofSeconds(10));
                waits.until(ExpectedConditions.elementToBeClickable(item));
                item.click();
                break;
            }
        }
    }

    public void clickOnApply(){
        wait.until(ExpectedConditions.elementToBeClickable(applyButton)).click();
    }

        public  void findRatedE() throws InterruptedException {

            ScrollerHandler scrollerHandler = new ScrollerHandler(driver);
            WebElement returnElemt = scrollerHandler.findElementWithSubtitle("E");

            if (returnElemt != null) {
                try {
                    returnElemt.click();
                } catch (NoSuchElementException e) {
                    System.out.println("Element found but failed ");
                }
            } else {
                System.out.println("No matching element found for subtitle: ");
            }
        }
}
