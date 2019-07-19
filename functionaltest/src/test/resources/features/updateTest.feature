@updateTest
Feature: Update person information
  As a user
  I want to be able to edit my personal information

  Scenario: Edit Description
    Given I go to "home" page
    When I fill in "username" with "test_user"
    And I fill in "password" with "PASSWORD"
    And I click on the "login" button
    And I should be on "account profile" page
    And I click on the "edit" button
    And I should be on "account profile edit" page
    And I fill in "description" with "new description for test_user"
    And I click on the "save" button
    Then I should be on "account profile" page
    And I see in the field "description" there is "Description: new description for test_user"