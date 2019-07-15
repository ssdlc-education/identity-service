@loginTest
Feature: Login account
  As a user
  I want to be able to log into my account
  In order to see my personal information

  Background: Navigate to login page
    Given I go to the page with URL "http://localhost:5000"

  Scenario Outline: Successful Login
    When I fill in "username" with "<username>"
    And I fill in "password" with "<password>"
    And I click on the "login" button
    Then I should be on the page with URL "http://localhost:5000/account/view"
    And I see in the field "username" there is "<usrname>"
    And I see in the field "email" there is "<email>"
    And I see in the field "description" there is "<description>"
    And I see in the field "firstname" there is "<firstname>"
    And I see in the field "lastname" there is "<lastname>"
    Examples:
    | username | password | usrname | firstname | lastname | email | description |
    | Alice    | PASSWORD | Username: Alice   | Firstname: Alice     | Lastname: Demo     | Email: alice@gmail.com | Description: Test account for Alice |
    | Bob    | PASSWORD | Username: Bob   | Firstname: Bob     | Lastname: Demo     | Email: Bob@gmail.com | Description: Test account for Bob |

  Scenario: Failed Login
    When I fill in "username" with "Alice"
    And I fill in "password" with "PASS"
    And I click on the "login" button
    Then I see the error message "Account not found or incorrect password"

  Scenario: Logout
    When I fill in "username" with "Alice"
    And I fill in "password" with "PASSWORD"
    And I click on the "login" button
    And I should be on the page with URL "http://localhost:5000/account/view"
    And I click on the "logout" button
    Then I should be on the page with URL "http://localhost:5000/account/login"
#
#  Scenario: Blocked Login
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASS"
#    And I click on the "login" button
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASS"
#    And I click on the "login" button
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASS"
#    And I click on the "login" button
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASS"
#    And I click on the "login" button
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASS"
#    And I click on the "login" button
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASS"
#    And I click on the "login" button
#    When I fill in "username" with "Alice"
#    And I fill in "password" with "PASSWORD"
#    And I click on the "login" button
#    Then I should be on the page with URL "http://localhost:5000/account/login"
#    Then I see the error message "Account not found or incorrect password"

