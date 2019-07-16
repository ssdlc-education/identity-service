@registerTest
Feature: Sign up new account
  As a user
  I want to be able to register for new accounts

  Scenario Outline: Successful Registration
    Given I go to the page with URL "http://localhost:5000"
    And I click on the "create" button
    And I should be on the page with URL "http://localhost:5000/account/register"
    When I fill in "username" with "<username>"
    And I fill in "email" with "<email>"
    And I fill in "firstname" with "<firstname>"
    And I fill in "lastname" with "<lastname>"
    And I fill in "password" with "<password>"
    And I fill in "description" with "<description>"
    And I click on the "register" button
    Then I should be on the page with URL "http://localhost:5000/account/view"
    And I see in the field "username" there is "<user>"
    And I see in the field "firstname" there is "<first>"
    And I see in the field "lastname" there is "<last>"
    And I see in the field "email" there is "<mail>"
    And I see in the field "description" there is "<desc>"
    Examples:
      | username  | password | firstname | lastname | email               | description            | user                | first            | last           | mail                       | desc                                |
      | test_user | PASSWORD | test      | user     | test_user@gmail.com | Test account           | Username: test_user | FirstName: test  | LastName: user | Email: test_user@gmail.com | Description: Test account           |
      | Alice     | PASSWORD | Alice     | Demo     | alice@gmail.com     | Test account for Alice | Username: Alice     | FirstName: Alice | LastName: Demo | Email: alice@gmail.com     | Description: Test account for Alice |
      | Bob       | PASSWORD | Bob       | Demo     | bob@gmail.com       | Test account for Bob   | Username: Bob       | FirstName: Bob   | LastName: Demo | Email: bob@gmail.com       | Description: Test account for Bob   |

