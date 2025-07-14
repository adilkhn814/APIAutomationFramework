@fi @regression
Feature: Financial Information Request and Fetch for Account Aggregator Platform
  As a Financial Information User (FIU)
  I want to request and fetch financial information
  So that I can access customer's financial data for authorized purposes

  Background:
    Given the Account Aggregator platform is available
    And I have valid authentication credentials
    And WireMock service is running for external dependencies
    And I have a valid active consent

  @smoke @positive @fi-request
  Scenario: Request financial information successfully
    Given I have a valid consent handle "consent_12345"
    And I have a valid FI data range from "2023-01-01" to "2024-01-01"
    And I have valid key material for encryption
    When I request financial information
    Then the FI request should be submitted successfully
    And the response status code should be 200
    And the response should contain a valid session ID
    And the response should contain the consent handle

  @positive @comprehensive @fi-request
  Scenario: Request financial information with comprehensive data
    Given I have a valid consent handle "consent_comprehensive_67890"
    And I have a FI data range from "2022-01-01" to "2024-12-31"
    And I have valid encryption parameters:
      | cryptoAlg | curve  | params         |
      | ECDH      | Curve25519 | base64params |
    And I have a valid digital signature for the consent
    When I request financial information with all details
    Then the FI request should be submitted successfully
    And the response should contain session information
    And the encryption parameters should be acknowledged

  @positive @fi-fetch
  Scenario: Fetch financial information successfully
    Given I have submitted a FI request successfully
    And the FI request has a valid session ID "session_12345"
    And the financial data is ready for fetch
    When I fetch the financial information
    Then the FI fetch should be successful
    And the response status code should be 200
    And the response should contain encrypted financial data
    And the response should contain FIP information
    And the response should contain masked account numbers

  @negative @validation @fi-request
  Scenario: Fail to request FI with invalid consent handle
    Given I have an invalid consent handle "invalid_consent"
    And I have a valid FI data range from "2023-01-01" to "2024-01-01"
    When I request financial information
    Then the FI request should fail
    And the response status code should be 400 or 404
    And the response should contain error message about invalid consent

  @negative @validation @fi-request
  Scenario: Fail to request FI with expired consent
    Given I have an expired consent handle "expired_consent_123"
    And I have a valid FI data range from "2023-01-01" to "2024-01-01"
    When I request financial information
    Then the FI request should fail
    And the response status code should be 400 or 403
    And the response should contain error message about expired consent

  @negative @validation @fi-fetch
  Scenario: Fail to fetch FI with invalid session ID
    Given I have an invalid session ID "invalid_session"
    When I fetch the financial information
    Then the FI fetch should fail
    And the response status code should be 400 or 404
    And the response should contain error message about invalid session

  @performance @fi-request
  Scenario: FI request should meet performance requirements
    Given I have a valid consent handle "consent_performance_test"
    And I have a valid FI data range from "2023-01-01" to "2024-01-01"
    When I request financial information
    Then the FI request should be submitted successfully
    And the response time should be less than 3000 milliseconds
    And the total processing time should be less than 8000 milliseconds

  @security @encryption @fi-request
  Scenario: Validate encryption parameters in FI request
    Given I have a valid consent handle "consent_encryption_test"
    And I have a FI data range from "2023-01-01" to "2024-01-01"
    And I have encryption parameters:
      | cryptoAlg | curve      | nonce        |
      | ECDH      | Curve25519 | random_nonce |
    When I request financial information with encryption
    Then the FI request should be submitted successfully
    And the encryption parameters should be validated
    And the response should confirm encryption setup

  @data-driven @fi-request
  Scenario Outline: Request FI for different date ranges
    Given I have a valid consent handle "consent_data_range_test"
    And I have a FI data range from "<from_date>" to "<to_date>"
    When I request financial information
    Then the FI request should be processed
    And the response status code should be "<expected_status>"

    Examples:
      | from_date  | to_date    | expected_status |
      | 2023-01-01 | 2023-12-31 | 200            |
      | 2022-06-01 | 2023-06-01 | 200            |
      | 2024-01-01 | 2024-03-31 | 200            |
      | 2025-01-01 | 2025-12-31 | 400            |

  @integration @workflow
  Scenario: Complete FI request and fetch workflow
    Given I have a valid consent handle "consent_workflow_test"
    And I have a valid FI data range from "2023-01-01" to "2024-01-01"
    When I request financial information
    And I wait for the data to be processed
    And I fetch the financial information
    Then the complete workflow should be successful
    And I should have access to the financial data
    And the data should be properly encrypted
    And the account information should be masked

  @callback @notification
  Scenario: Handle FI status notifications via callback
    Given I have submitted a FI request successfully
    And the system is configured to send callbacks
    When the FI processing status changes
    Then I should receive a callback notification
    And the callback should contain session information
    And the callback should contain FI status updates
    And the callback should contain FIP processing status