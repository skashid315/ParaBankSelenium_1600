Feature: ParaBank Login Module
  As a user of ParaBank
  I want to log in to my account
  So that I can access my banking services

  Background:
    Given the user is on the ParaBank login page

  @Smoke @Regression @HighPriority @first
  Scenario: AUTH-001 - Successful login with valid credentials
    When the user enters username "testuser"
    And the user enters password "Test@123"
    And the user clicks the Login button
    Then the user should be redirected to the Accounts Overview page
    And the Accounts Overview header should be displayed

  @Regression @Negative @HighPriority
  Scenario: AUTH-002 - Login with invalid password
    When the user enters username "testuser"
    And the user enters password "WrongPass123"
    And the user clicks the Login button
    Then an error message should be displayed
    And the error message should contain "could not be verified"
    And the user should remain on the login page

  @Regression @Negative @HighPriority
  Scenario: AUTH-003 - Login with non-existent username
    When the user enters username "nonexistent123"
    And the user enters password "Test@123"
    And the user clicks the Login button
    Then an error message should be displayed
    And the error message should contain "could not be verified"
    And the user should remain on the login page

  @Regression @Negative @MediumPriority
  Scenario: AUTH-004 - Login with empty username field
    When the user leaves the username field empty
    And the user enters password "Test@123"
    And the user clicks the Login button
    Then the user should remain on the login page

  @Regression @Negative @MediumPriority
  Scenario: AUTH-005 - Login with empty password field
    When the user enters username "testuser"
    And the user leaves the password field empty
    And the user clicks the Login button
    Then the user should remain on the login page

  @Regression @Security @Critical
  Scenario: AUTH-008 - SQL Injection attempt in username
    When the user enters username "' OR '1'='1"
    And the user enters password "Test@123"
    And the user clicks the Login button
    Then the login should fail
    And no unauthorized access should be granted
    And the user should remain on the login page

  @Regression @Security @Critical
  Scenario: AUTH-009 - XSS attempt in username
    When the user enters username "<script>alert('xss')</script>"
    And the user enters password "Test@123"
    And the user clicks the Login button
    Then the login should fail
    And the script should not be executed
    And the user should remain on the login page

  @Regression @Functional @MediumPriority
  Scenario: AUTH-012 - Password masking verification
    When the user checks the password field type
    Then the password field should have type "password"
    And the password characters should be masked

  @Smoke @Regression @Functional @HighPriority
  Scenario: AUTH-013 - Successful logout and session termination
    Given the user is logged in with valid credentials
    When the user clicks the Log Out link
    Then the user should be redirected to the login page
    And the Customer Login header should be displayed
