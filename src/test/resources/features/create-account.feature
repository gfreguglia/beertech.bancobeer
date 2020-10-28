Feature: Manage account
  Scenario: Client wants to manage his account
    When client makes POST to /conta
    Then client receives response code 200
  Scenario:
    When client makes PATCH to /conta
    Then client receives response code 204