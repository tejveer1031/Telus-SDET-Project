package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.SelectanimatedPage;
import utilities.DriverManager;

public class SelectAnimatedSteps {
    private SelectanimatedPage selectanimatedPage;



    @Before
    public void setUp() {
        selectanimatedPage = new SelectanimatedPage(DriverManager.getDriver());
    }


    @When("user click on-demand movies")
    public void userClickOnDemandMovies() {
        selectanimatedPage.clickOnDemandMovies();
    }

    @Then("on-demand movies and Tv show should appear")
    public void onDemandMoviesAndTvShowShouldAppear() {
            Assert.assertTrue(selectanimatedPage.onDemandSelected(),"User not able to click on-demand movies");
    }

    @When("the user scrolls down to the movies section")
    public void theUserScrollsDownToTheMoviesSection(){
            selectanimatedPage.scrollToMovies();
    }

    @And("Select movies section to view all")
    public void selectMoviesSectionToViewAll() {
        selectanimatedPage.clickToViewAll();
    }


    @Then("user must be on movie page")
    public void userMustBeOnMoviePage() {
        Assert.assertTrue(selectanimatedPage.verifyUserMustBeOnMovies(), "User is not on the Movies page as expected.");
    }


    @When("user select animated from movies")
    public void userSelectAnimatedFromMovies() {
       selectanimatedPage.chooseAnimatedMovies();
    }

    @When("user click on filter button")
    public void userClickOnFilterButton() {
        selectanimatedPage.filterAnimatedMovies();
    }

    @Then("user apply the filter")
    public void userApplyTheFilter() {
        selectanimatedPage.clickOnApply();
    }

    @When("user find rated E and click")
    public void userFindRatedE() throws InterruptedException {
        selectanimatedPage.findRatedE();
    }


}
