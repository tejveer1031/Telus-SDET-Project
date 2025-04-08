@popUp @smoke
Feature: Close Welcome Pop-up
  As a user
  I want to close the welcome pop-up
  So I can interact with the main website content

  Background:
    Given I am on the home page

  @valid
  Scenario: Successfully close the welcome pop-up
    When I close the welcome pop-up
    Then The pop-up should not be displayed