@selectAnimated @smoke
  Feature: Select the animated movies
    As a user, I want to select animated movies by performing certain actions.


    @valid
    Scenario: user select on demand movies
      Given I am on the home page
      When user click on-demand movies
      Then on-demand movies and Tv show should appear


    @valid
    Scenario: user select movies
      When the user scrolls down to the movies section
      And  Select movies section to view all
      Then user must be on movie page

    @valid
    Scenario: User filter the movies
      When user click on filter button
      And user select animated from movies
      Then user apply the filter

    @valid
    Scenario: User able to find movie with rated E
     When user find rated E and click
