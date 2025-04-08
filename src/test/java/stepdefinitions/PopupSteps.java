package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.HomePage;
import utilities.ConfigReader;
import utilities.DriverManager;

public class PopupSteps {
    private HomePage homePage;

    @Given("I am on the home page")
    public void navigateToHomePage() {
        DriverManager.getDriver().get(ConfigReader.getProperty("base.url"));
        homePage = new HomePage(DriverManager.getDriver());
    }

    @When("I close the welcome pop-up")
    public void closePopup() {
        homePage.closeWelcomePopup();
    }

    @Then("The pop-up should not be displayed")
    public void verifyPopupClosed() {
        Assert.assertFalse(homePage.isPopupDisplayed(),
                "Welcome pop-up is still visible after closing");
    }




}
