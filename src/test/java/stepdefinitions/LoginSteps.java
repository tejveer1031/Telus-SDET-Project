package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import pages.LoginPage;
import utilities.DriverManager;

public class LoginSteps {
    private LoginPage loginPage;

    @Before
    public void setUp(){
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @Given("User click on login button")
    public void userClickOnLoginButton() {
        loginPage.clickOnLogin();
    }

}
