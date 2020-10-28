Feature: Make a deposit
  Scenario: Client sets up an account
    When client makes POST to /conta
    Then client receives response code 200

  Scenario: Client logs in the account
    When client makes POST to /authenticate
    Then client receives response code 200

  Scenario: Client makes a deposit to own account
    Given client is logged in the account
    When client sends 50 deposit POST to /operacao
    Then client receives response code 200

  Scenario: Client views current balance
    Given client is logged in the account
    When client sends GET to /conta/uuid/saldo
    Then client receives response code 200
    And current balance is 50
