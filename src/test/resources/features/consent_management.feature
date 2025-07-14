@consent @regression
Feature: Consent Management for Account Aggregator Platform
  As a Financial Information User (FIU)
  I want to create and manage consent requests
  So that I can access customer's financial information with proper authorization

  Background:
    Given the Account Aggregator platform is available
    And I have valid authentication credentials
    And WireMock service is running for external dependencies

  @smoke @positive
  Scenario: Create a basic consent request successfully
    Given I have a valid customer with ID "customer@example.com"
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    When I create a basic consent request for the customer
    Then the consent request should be created successfully
    And the response status code should be 200
    And the response should contain a valid consent handle
    And the customer ID in response should match "customer@example.com"
    And the consent status should be "READY"

  @positive @comprehensive
  Scenario: Create a comprehensive consent request with all fields
    Given I have a valid customer with ID "customer@example.com"
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    And I have consent types "PROFILE,SUMMARY,TRANSACTIONS"
    And I have FI types "DEPOSIT,TERM_DEPOSIT,SIP"
    And I have purpose code "101" with description "Wealth management service"
    When I create a comprehensive consent request with all details
    Then the consent request should be created successfully
    And the response status code should be 200
    And the response should contain a valid consent handle
    And the transaction ID should match between request and response
    And the consent should be valid for the specified purpose

  @negative @validation
  Scenario: Fail to create consent with invalid customer ID
    Given I have an invalid customer with empty ID ""
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    When I create a basic consent request for the customer
    Then the consent request should fail
    And the response status code should be 400 or 422
    And the response should contain validation error message

  @negative @validation
  Scenario: Fail to create consent with missing required fields
    Given I have a consent request with missing consent details
    When I send the incomplete consent request
    Then the consent request should fail
    And the response status code should be in the range 400 to 499
    And the response should indicate missing required fields

  @performance
  Scenario: Consent creation should meet performance requirements
    Given I have a valid customer with ID "customer@example.com"
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    When I create a basic consent request for the customer
    Then the consent request should be created successfully
    And the response time should be less than 5000 milliseconds
    And the total execution time should be less than 10000 milliseconds

  @validation @status
  Scenario Outline: Validate different consent statuses
    Given I have a valid customer with ID "customer@example.com"
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    When I create a basic consent request for the customer
    Then the consent request should be created successfully
    And the consent status should be one of "<valid_statuses>"

    Examples:
      | valid_statuses                                           |
      | PENDING,READY,ACTIVE,PAUSED,EXPIRED,DENIED,REVOKED      |

  @data-driven
  Scenario Outline: Create consent for different customer types
    Given I have a customer with ID "<customer_id>"
    And I have a FIU ID "<fiu_id>"
    And I have a FIP ID "<fip_id>"
    When I create a basic consent request for the customer
    Then the consent request should be processed
    And the response status code should be "<expected_status>"

    Examples:
      | customer_id           | fiu_id          | fip_id          | expected_status |
      | customer@example.com  | test-fiu-001    | test-fip-001    | 200            |
      | premium@example.com   | premium-fiu-001 | premium-fip-001 | 200            |
      | business@example.com  | business-fiu-001| business-fip-001| 200            |

  @integration @callback
  Scenario: Consent callback workflow
    Given I have created a consent request successfully
    And the consent has a valid consent handle
    When the consent status is updated via callback
    Then the consent should reflect the updated status
    And appropriate notifications should be triggered

  @security @authentication
  Scenario: Create consent with invalid authentication
    Given I have invalid or expired authentication token
    And I have a valid customer with ID "customer@example.com"
    When I try to create a consent request
    Then the request should be rejected
    And the response status code should be 401 or 403
    And the response should indicate authentication failure

  @boundary @limits
  Scenario: Validate consent request field limits
    Given I have a customer with very long ID "customer_with_extremely_long_identifier_that_exceeds_normal_limits@example.com"
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    When I create a basic consent request for the customer
    Then the system should handle the request appropriately
    And the response should indicate whether the field length is acceptable