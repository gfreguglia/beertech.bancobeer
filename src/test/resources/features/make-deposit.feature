Feature: Make a deposit
  Scenario: Client sets up an account
    When client creates an account
    Then client receives response code 200

  Scenario: Client logs in the account
    When client authenticates
    Then client receives response code 200

  Scenario: Client makes a deposit to own account
    Given client is logged in the account
    When client makes deposit of 50 moneys into his account
    Then client receives response code 200

  Scenario: Client views current balance
    Given client is logged in the account
    When client views his balance
    Then client receives response code 200
    And current balance is 50
