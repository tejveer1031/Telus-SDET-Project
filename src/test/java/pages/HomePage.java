package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriverWait wait;

    // Pop-up elements
    @FindBy(css = "div.popUpContainer.welcome-modal-popup")
    private WebElement popupContainer;

    @FindBy(css = "button.imageButton[type='button'] > img[src='/images/cross-icon.svg']")
    private WebElement closeButton;

    public HomePage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void closeWelcomePopup() {
        wait.until(ExpectedConditions.visibilityOf(popupContainer));
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    public boolean isPopupDisplayed() {
        try {
            return popupContainer.isDisplayed();
        } catch (Exception e) {
            return false;  // here i want to return false because popUp is not displayed
        }
    }


}
