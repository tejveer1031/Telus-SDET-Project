package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.DriverManager;

import java.time.Duration;

public class LoginPage {
    private final WebDriverWait wait;

    @FindBy(css = "button.login-button")
    WebElement homePageLoginButton;

    @FindBy(css = "button.imageButton[type='button'] > img[src='/images/search-icon.png']")
    WebElement serachButton;

    @FindBy(css = "input.search-input[type='text']")
    WebElement searchIput;

    public LoginPage(WebDriver driver) {
       this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver,this);
    }


    public void clickOnLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(homePageLoginButton)).click();
//            wait.until(ExpectedConditions.elementToBeClickable(serachButton)).click();
//            wait.until(ExpectedConditions.visibilityOf(searchIput)).sendKeys("movies");
    }
}
