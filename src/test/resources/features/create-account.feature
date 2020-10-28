Feature: Manage account
  Scenario: Client wants to manage account
    When client sends request to create account
    Then client receives response code 200
  Scenario: Client wants to change password
    When client sends request with new password
    Then client receives response code 204