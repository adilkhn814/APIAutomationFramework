# Account Aggregator (AA) BDD Test Automation Framework

A comprehensive **Behavior-Driven Development (BDD)** test automation framework for Account Aggregator platform APIs, built with **Cucumber**, **Java**, and modern testing tools. This framework allows **anyone** to understand and write test scripts using **natural language**.

## 🌟 Why BDD?

- ✅ **Natural Language**: Write tests in plain English using Gherkin syntax
- ✅ **Business Readable**: Non-technical stakeholders can understand test scenarios
- ✅ **Living Documentation**: Tests serve as up-to-date documentation
- ✅ **Collaborative**: Business analysts, developers, and testers can contribute
- ✅ **Maintainable**: Easy to update and extend test scenarios

## 🏗️ Framework Architecture

```
src/
├── main/java/com/aa/
│   ├── base/           # Base test classes and utilities
│   ├── pojo/           # POJOs for request/response objects
│   ├── service/        # API service classes (ConsentService, etc.)
│   └── utils/          # Utility classes (ConfigManager, TokenManager, etc.)
└── test/
    ├── java/com/aa/bdd/
    │   ├── runner/         # Cucumber test runners
    │   ├── stepdefinitions/ # Step definition classes
    │   └── hooks/          # Setup and teardown hooks
    └── resources/
        ├── features/       # Gherkin feature files (.feature)
        ├── config/         # Configuration files
        └── schemas/        # JSON schema files for validation
```

## 🛠️ Technology Stack

- **Java**: 11+ - Programming language
- **Cucumber**: 7.18.0 - BDD testing framework
- **TestNG**: 7.8.0 - Test execution framework
- **RestAssured**: 5.4.0 - API testing library
- **Gson**: 2.10.1 - JSON serialization/deserialization
- **ExtentReports**: 5.0.9 - HTML reporting with Cucumber integration
- **WireMock**: 2.35.1 - Mock external services
- **Maven**: Build and dependency management

## 🚀 Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git
- Basic understanding of Gherkin syntax (optional - you'll learn quickly!)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd account-aggregator-bdd-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Run smoke tests**
   ```bash
   mvn test -Dcucumber.filter.tags="@smoke"
   ```

## 📝 Writing Your First Test

### Example Feature File

```gherkin
@consent @smoke
Feature: Consent Management
  As a Financial Information User (FIU)
  I want to create consent requests
  So that I can access customer's financial information

  Scenario: Create a basic consent successfully
    Given I have a valid customer with ID "customer@example.com"
    And I have a valid FIU ID "test-fiu-001"
    And I have a valid FIP ID "test-fip-001"
    When I create a basic consent request for the customer
    Then the consent request should be created successfully
    And the response status code should be 200
    And the response should contain a valid consent handle
```

### How to Add New Scenarios

1. **Open a feature file** in `src/test/resources/features/`
2. **Write your scenario** using Given-When-Then format
3. **Run the test** - Cucumber will generate step definitions for you
4. **Implement step definitions** in the appropriate step definition class

## 🎯 Available Feature Files

### 1. Consent Management (`consent_management.feature`)
```gherkin
@consent @regression
Feature: Consent Management for Account Aggregator Platform
  
  # Scenarios include:
  - Create basic consent request
  - Create comprehensive consent with all fields
  - Handle invalid customer ID
  - Validate missing required fields
  - Performance testing
  - Status validation
  - Data-driven testing with examples
```

### 2. Financial Information (`financial_information.feature`)
```gherkin
@fi @regression
Feature: Financial Information Request and Fetch
  
  # Scenarios include:
  - Request financial information
  - Fetch financial data
  - Handle invalid consent handles
  - Encryption parameter validation
  - Complete workflow testing
```

## 🏃‍♂️ Running Tests

### Run by Tags

```bash
# Smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Consent-related tests
mvn test -Dcucumber.filter.tags="@consent"

# Financial Information tests
mvn test -Dcucumber.filter.tags="@fi"

# Performance tests
mvn test -Dcucumber.filter.tags="@performance"

# Multiple tags
mvn test -Dcucumber.filter.tags="@smoke and @consent"
```

### Run Specific Features

```bash
# Run only consent management tests
mvn test -Dtest=ConsentTestRunner

# Run only financial information tests
mvn test -Dtest=FinancialInformationTestRunner

# Run all BDD tests
mvn test -Dtest=CucumberTestRunner
```

### Run with Different Environments

```bash
# Staging environment
mvn test -Denvironment=STAGING -Dbase.url=https://staging-api.example.com

# Production environment
mvn test -Denvironment=PROD -Dbase.url=https://api.example.com
```

## 📊 Reports and Results

### Cucumber Reports
- **HTML Report**: `target/cucumber-reports/cucumber-html-report/index.html`
- **JSON Report**: `target/cucumber-reports/cucumber.json`
- **ExtentReports**: `target/cucumber-reports/extent-spark-report.html`

### Viewing Reports
```bash
# Open Cucumber HTML report
open target/cucumber-reports/cucumber-html-report/index.html

# Open ExtentReports (enhanced with screenshots and logs)
open target/cucumber-reports/extent-spark-report.html
```

## 🎨 Understanding Gherkin Syntax

### Basic Keywords

- **Feature**: High-level description of a software feature
- **Scenario**: Specific example of business rule
- **Given**: Context or precondition
- **When**: Action or event
- **Then**: Expected outcome
- **And**: Additional conditions
- **But**: Negative conditions

### Example with Data Tables

```gherkin
Scenario: Create consent for different customer types
  Given I have encryption parameters:
    | cryptoAlg | curve      | nonce        |
    | ECDH      | Curve25519 | random_nonce |
  When I request financial information with encryption
  Then the encryption parameters should be validated
```

### Example with Scenario Outline

```gherkin
Scenario Outline: Create consent for different customers
  Given I have a customer with ID "<customer_id>"
  When I create a basic consent request for the customer
  Then the response status code should be "<expected_status>"
  
  Examples:
    | customer_id           | expected_status |
    | customer@example.com  | 200            |
    | premium@example.com   | 200            |
    | business@example.com  | 200            |
```

## 🏷️ Using Tags Effectively

### Test Categories
- **@smoke**: Quick, essential tests
- **@regression**: Full test suite
- **@positive**: Happy path scenarios
- **@negative**: Error handling scenarios
- **@performance**: Performance validation
- **@security**: Security testing
- **@integration**: End-to-end workflows

### Feature Tags
- **@consent**: Consent management tests
- **@fi**: Financial Information tests
- **@callback**: Callback handling tests

### Example Tagging Strategy
```gherkin
@consent @smoke @positive
Scenario: Create basic consent successfully
  # This scenario will run in smoke and regression suites
```

## 📝 Adding New Test Scenarios

### Step 1: Write the Scenario
```gherkin
@consent @new-feature
Scenario: Validate consent expiry
  Given I have a consent that expires in 30 days
  When I check the consent status after 31 days
  Then the consent should be expired
  And I should not be able to use it for FI requests
```

### Step 2: Run and Generate Step Definitions
```bash
mvn test -Dcucumber.filter.tags="@new-feature"
```

Cucumber will suggest step definitions like:
```java
@Given("I have a consent that expires in {int} days")
public void iHaveAConsentThatExpiresInDays(int days) {
    // Implementation needed
}
```

### Step 3: Implement Step Definitions
Add the implementation in the appropriate step definition class.

## 🔧 Configuration

### Feature-Specific Configuration

```properties
# src/test/resources/config/config.properties

# API Configuration
base.url=https://api.account-aggregator.com
environment=TEST

# Test Data
test.customer.id=customer@example.com
test.fiu.id=test-fiu-001
test.fip.id=test-fip-001

# Performance Thresholds
performance.response.threshold.ms=5000
performance.total.threshold.ms=10000
```

### Tags Configuration

You can configure which tags to run in different environments:

```xml
<!-- Maven profiles for different tag combinations -->
<profile>
    <id>smoke</id>
    <properties>
        <cucumber.tags>@smoke</cucumber.tags>
    </properties>
</profile>
```

## 🎯 Best Practices for Writing BDD Tests

### 1. Write User-Focused Scenarios
```gherkin
# Good: Business-focused
Scenario: Customer successfully creates consent for bank account access
  Given I am a customer with valid bank accounts
  When I provide consent for the FIU to access my account information
  Then I should receive a confirmation of consent creation

# Avoid: Technical implementation details
Scenario: POST /consent returns 200 with valid JSON
```

### 2. Use Domain Language
```gherkin
# Good: Domain terminology
Given I have a valid Financial Information User
When I create a consent for account aggregation

# Avoid: Technical jargon
Given I have a valid HTTP client
When I POST to the consent endpoint
```

### 3. Keep Scenarios Independent
Each scenario should be able to run independently without depending on other scenarios.

### 4. Use Descriptive Scenario Names
```gherkin
# Good
Scenario: Consent creation fails when customer ID is missing

# Less clear
Scenario: Test consent validation
```

## 🚀 CI/CD Integration

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Smoke Tests') {
            steps {
                sh 'mvn test -Dcucumber.filter.tags="@smoke"'
            }
        }
        stage('Regression Tests') {
            when { branch 'main' }
            steps {
                sh 'mvn test -Dcucumber.filter.tags="@regression"'
            }
        }
    }
    post {
        always {
            publishHTML([
                reportDir: 'target/cucumber-reports',
                reportFiles: '**/*.html',
                reportName: 'Cucumber BDD Report'
            ])
        }
    }
}
```

### GitHub Actions
```yaml
name: BDD Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
    - name: Run BDD Tests
      run: mvn test -Dcucumber.filter.tags="@smoke or @regression"
    - name: Upload Reports
      uses: actions/upload-artifact@v2
      with:
        name: cucumber-reports
        path: target/cucumber-reports/
```

## 🐛 Troubleshooting

### Common Issues

1. **Step Definition Not Found**
   ```
   Error: Undefined step: "I have a new scenario step"
   ```
   **Solution**: Run the test, copy the generated step definition, and implement it.

2. **Tags Not Working**
   ```bash
   # Make sure to use proper tag syntax
   mvn test -Dcucumber.filter.tags="@smoke"  # Correct
   mvn test -Dcucumber.filter.tags="smoke"   # Wrong (missing @)
   ```

3. **Feature Files Not Found**
   ```
   Error: No features found
   ```
   **Solution**: Ensure feature files are in `src/test/resources/features/`

### Debug Mode
```bash
# Enable debug logging
mvn test -Dcucumber.filter.tags="@smoke" -Dlog.level=DEBUG

# Dry run (check syntax without execution)
mvn test -Dcucumber.options="--dry-run"
```

## 📚 Learning Resources

### Gherkin Documentation
- [Cucumber Gherkin Reference](https://cucumber.io/docs/gherkin/reference/)
- [Writing Good Gherkin](https://cucumber.io/docs/bdd/better-gherkin/)

### BDD Best Practices
- [BDD Fundamentals](https://cucumber.io/docs/bdd/)
- [Effective BDD](https://cucumber.io/blog/bdd/effective-bdd/)

## 🤝 Contributing Test Scenarios

### For Business Analysts
1. Write scenarios in plain English using Gherkin
2. Focus on business value and user needs
3. Use examples to clarify expected behavior
4. Collaborate with developers on technical details

### For Developers
1. Implement step definitions using existing service layer
2. Keep step definitions reusable and maintainable
3. Add proper error handling and logging
4. Follow existing code patterns

### For Testers
1. Design comprehensive test scenarios covering edge cases
2. Add appropriate tags for test categorization
3. Validate test data and expected outcomes
4. Ensure tests are reliable and maintainable

## 🎉 Example: Complete Workflow

Here's how to add a complete new test scenario:

### 1. Business Requirement
"As a customer, I want to ensure my consent expires automatically after the specified time to maintain security."

### 2. Write Feature
```gherkin
@consent @security @expiry
Feature: Consent Expiry Validation
  As a customer
  I want my consent to expire automatically
  So that my financial data remains secure

  Scenario: Consent expires after specified duration
    Given I have created a consent that expires in 1 day
    When I wait for 1 day and 1 hour
    And I try to use the consent for financial data access
    Then the consent should be expired
    And the system should reject the financial data request
    And I should receive an error indicating expired consent
```

### 3. Run and Implement
```bash
mvn test -Dcucumber.filter.tags="@expiry"
# Copy generated step definitions and implement them
```

### 4. Execute and Report
```bash
mvn test -Dcucumber.filter.tags="@security"
# View results in target/cucumber-reports/extent-spark-report.html
```

## 🏆 Framework Benefits

### For Business Stakeholders
- ✅ **Readable Tests**: Understand what's being tested without technical knowledge
- ✅ **Living Documentation**: Tests reflect current system behavior
- ✅ **Requirements Validation**: Ensure features meet business needs

### For Developers
- ✅ **Reusable Components**: Step definitions use existing service layer
- ✅ **Maintainable Code**: Clean separation between business logic and test implementation
- ✅ **Comprehensive Coverage**: Easy to add new test scenarios

### For Testers
- ✅ **Natural Language**: Write tests without complex programming
- ✅ **Data-Driven Testing**: Use examples and scenario outlines
- ✅ **Rich Reporting**: Detailed reports with steps, screenshots, and logs

---

**Ready to start testing? Write your first scenario in Gherkin and see the magic of BDD! 🎯**

**For questions and support:**
- Check existing feature files for examples
- Review step definitions for available steps
- Create new scenarios using the Given-When-Then format
- Run tests and let Cucumber guide you through the implementation


