# 🚀 Getting Started with Account Aggregator BDD Framework

This guide will help you get up and running with the BDD framework in **5 minutes**!

## 📋 Prerequisites Checklist

- [ ] Java 11 or higher installed
- [ ] Maven 3.6+ installed  
- [ ] IDE with Cucumber plugin (IntelliJ IDEA recommended)
- [ ] Basic understanding of Gherkin (don't worry, it's easy!)

## 🏃‍♂️ Quick Setup (5 Minutes)

### Step 1: Clone and Setup
```bash
# Clone the project
git clone <your-repository-url>
cd account-aggregator-bdd-framework

# Install dependencies
mvn clean install

# Verify setup
./verify-bdd-framework.sh
```

### Step 2: Run Your First Test
```bash
# Run smoke tests (fastest)
mvn test -Dcucumber.filter.tags="@smoke"

# View results
open target/cucumber-reports/extent-spark-report.html
```

### Step 3: Explore Reports
1. **HTML Report**: Beautiful, interactive reports with step details
2. **ExtentReports**: Rich reports with screenshots and logs
3. **Console Output**: Real-time test execution logs

## 📝 Writing Your First Test (Step by Step)

### Example 1: Simple Consent Test

1. **Open feature file**: `src/test/resources/features/consent_management.feature`

2. **Add your scenario**:
```gherkin
@smoke @my-test
Scenario: My first BDD test for consent creation
  Given I have a valid customer with ID "john.doe@example.com"
  And I have a valid FIU ID "my-fiu-001"
  And I have a valid FIP ID "my-fip-001"
  When I create a basic consent request for the customer
  Then the consent request should be created successfully
  And the response status code should be 200
```

3. **Run your test**:
```bash
mvn test -Dcucumber.filter.tags="@my-test"
```

4. **Check results** in the generated reports!

### Example 2: Data-Driven Test

Add this to your feature file:
```gherkin
@data-driven @my-test
Scenario Outline: Test multiple customers
  Given I have a customer with ID "<customer_id>"
  And I have a FIU ID "<fiu_id>"
  When I create a basic consent request for the customer
  Then the response status code should be "<expected_status>"

  Examples:
    | customer_id        | fiu_id      | expected_status |
    | alice@example.com  | fiu-001     | 200            |
    | bob@example.com    | fiu-002     | 200            |
    | carol@example.com  | fiu-003     | 200            |
```

## 🎯 Common Test Scenarios

### ✅ Happy Path Testing
```gherkin
@positive @consent
Scenario: Successful consent creation with all details
  Given I have a valid customer with ID "premium@example.com"
  And I have consent types "PROFILE,SUMMARY,TRANSACTIONS"
  And I have FI types "DEPOSIT,TERM_DEPOSIT"
  And I have purpose code "101" with description "Wealth management"
  When I create a comprehensive consent request with all details
  Then the consent request should be created successfully
  And the transaction ID should match between request and response
```

### ❌ Error Handling Testing
```gherkin
@negative @validation
Scenario: Consent creation fails with invalid customer
  Given I have an invalid customer with empty ID ""
  When I create a basic consent request for the customer
  Then the consent request should fail
  And the response status code should be 400 or 422
  And the response should contain validation error message
```

### ⚡ Performance Testing
```gherkin
@performance
Scenario: Consent creation meets performance requirements
  Given I have a valid customer with ID "perf-test@example.com"
  When I create a basic consent request for the customer
  Then the response time should be less than 5000 milliseconds
  And the total execution time should be less than 10000 milliseconds
```

## 🏷️ Tag-Based Execution

### Essential Tags
```bash
# Quick smoke tests (5-10 scenarios)
mvn test -Dcucumber.filter.tags="@smoke"

# All regression tests 
mvn test -Dcucumber.filter.tags="@regression"

# Only consent-related tests
mvn test -Dcucumber.filter.tags="@consent"

# Only financial information tests
mvn test -Dcucumber.filter.tags="@fi"

# Performance tests only
mvn test -Dcucumber.filter.tags="@performance"

# Negative/error tests only
mvn test -Dcucumber.filter.tags="@negative"
```

### Combining Tags
```bash
# Smoke tests for consent only
mvn test -Dcucumber.filter.tags="@smoke and @consent"

# All positive tests except performance
mvn test -Dcucumber.filter.tags="@positive and not @performance"

# Run specific feature combinations
mvn test -Dcucumber.filter.tags="@consent or @fi"
```

## 🛠️ IDE Setup

### IntelliJ IDEA (Recommended)
1. Install **Cucumber for Java** plugin
2. Install **Gherkin** plugin
3. Configure:
   - Go to Settings → Build → Build Tools → Maven → Runner
   - Set VM Options: `-Dcucumber.filter.tags="@smoke"`

### Visual Studio Code
1. Install **Cucumber (Gherkin) Full Support** extension
2. Install **Java Extension Pack**
3. Configure launch.json for test execution

### Eclipse
1. Install **Cucumber Eclipse Plugin**
2. Configure as Maven project
3. Set up run configurations for different tag combinations

## 📊 Understanding Reports

### Cucumber HTML Report
```
target/cucumber-reports/cucumber-html-report/index.html
```
- **Features**: Overview of all features
- **Scenarios**: Individual scenario results
- **Steps**: Detailed step execution
- **Tags**: Filter by tag categories

### ExtentReports (Enhanced)
```
target/cucumber-reports/extent-spark-report.html
```
- **Dashboard**: Executive summary
- **Tests**: Detailed test results with logs
- **Categories**: Group by tags
- **Timeline**: Execution timeline

### JSON Report (For CI/CD)
```
target/cucumber-reports/cucumber.json
```
- Machine-readable format
- Perfect for CI/CD integration
- Trend analysis over time

## 🔧 Configuration

### Environment-Specific Testing
```bash
# Test against different environments
mvn test -Denvironment=DEV -Dbase.url=https://dev-api.example.com
mvn test -Denvironment=STAGING -Dbase.url=https://staging-api.example.com
mvn test -Denvironment=PROD -Dbase.url=https://api.example.com
```

### Custom Configuration
Edit `src/test/resources/config/config.properties`:
```properties
# API Configuration
base.url=https://your-api.example.com
environment=TEST

# Performance Thresholds
performance.response.threshold.ms=3000
performance.total.threshold.ms=8000

# Test Data
test.customer.id=your-test@example.com
test.fiu.id=your-fiu-001
```

## 🚨 Troubleshooting

### Common Issues & Solutions

#### 1. Step Definition Not Found
```
Error: Undefined step: "I perform a new action"
```
**Solution**:
1. Run the test - Cucumber will generate step definitions
2. Copy the generated code
3. Add to appropriate step definition class
4. Implement the logic

#### 2. Feature File Not Found
```
Error: No features found
```
**Solution**:
- Ensure `.feature` files are in `src/test/resources/features/`
- Check file naming (must end with `.feature`)
- Verify proper Gherkin syntax

#### 3. Tests Not Running
```
No tests executed
```
**Solution**:
- Check tag syntax: `@smoke` not `smoke`
- Verify test runner configuration
- Ensure feature files have scenarios

#### 4. Compilation Errors
**Solution**:
```bash
# Clean and reinstall
mvn clean install

# Check Java version
java -version  # Should be 11+

# Update dependencies
mvn dependency:resolve
```

### Debug Mode
```bash
# Enable detailed logging
mvn test -Dcucumber.filter.tags="@smoke" -Dlog.level=DEBUG

# Dry run (syntax check only)
mvn test -Dcucumber.options="--dry-run"

# Generate step definitions
mvn test -Dcucumber.options="--dry-run --snippets=camelcase"
```

## 👥 Team Collaboration

### For Business Analysts
1. **Write scenarios** in plain English
2. **Use Examples** to clarify expected behavior
3. **Focus on business value**, not technical implementation
4. **Collaborate** with developers on step definitions

### For Developers
1. **Implement step definitions** using existing service layer
2. **Keep steps reusable** across multiple scenarios
3. **Add proper error handling** and logging
4. **Follow existing patterns** in the framework

### For Testers
1. **Design comprehensive scenarios** covering edge cases
2. **Use appropriate tags** for categorization
3. **Validate test data** and expected outcomes
4. **Maintain test reliability** and stability

## 📈 Best Practices

### Writing Good Scenarios
```gherkin
# ✅ Good: Business-focused
Scenario: Customer successfully creates consent for bank account access
  Given I am a customer with valid bank accounts
  When I provide consent for FIU to access my account information
  Then I should receive confirmation of consent creation

# ❌ Avoid: Technical implementation details  
Scenario: POST /consent returns 200 with valid JSON response
```

### Organizing Tests
```
features/
├── consent_management.feature      # Core consent functionality
├── financial_information.feature  # FI request/fetch operations
├── error_scenarios.feature        # Error handling tests
└── performance.feature            # Performance validation
```

### Step Definition Organization
```java
// Group related steps together
@Given("I have a valid customer with ID {string}")
@Given("I have a valid FIU ID {string}")
@Given("I have a valid FIP ID {string}")

// Reuse common actions
@When("I create a basic consent request for the customer")
@When("I create a comprehensive consent request with all details")

// Consistent assertions
@Then("the consent request should be created successfully")
@Then("the response status code should be {int}")
```

## 🎉 Next Steps

### 1. Explore Advanced Features
- Add database validation steps
- Implement callback testing
- Create custom assertions
- Add API contract testing

### 2. Extend the Framework
- Add new endpoints (FI/request, FI/fetch)
- Create UI testing capabilities
- Implement security testing scenarios
- Add load testing integration

### 3. CI/CD Integration
- Set up Jenkins/GitHub Actions
- Configure automated reporting
- Implement test result notifications
- Create deployment gates

### 4. Team Training
- Conduct BDD workshops
- Create team guidelines
- Establish review processes
- Share best practices

## 📚 Learning Resources

### Gherkin & BDD
- [Cucumber Gherkin Reference](https://cucumber.io/docs/gherkin/reference/)
- [BDD Fundamentals](https://cucumber.io/docs/bdd/)
- [Writing Effective Scenarios](https://cucumber.io/docs/bdd/better-gherkin/)

### Account Aggregator
- [AA Framework Specification](https://api.rebit.org.in/)
- [ReBIT Guidelines](https://www.rebit.org.in/)

## 🆘 Getting Help

1. **Check the README.md** for comprehensive documentation
2. **Review existing scenarios** in feature files for examples  
3. **Look at step definitions** to understand available steps
4. **Run verification script**: `./verify-bdd-framework.sh`
5. **Check logs** in `logs/` directory for detailed error information

---

**🎯 Ready to start? Pick a scenario from above and try it out!**

**Remember: BDD is about collaboration and communication. Start simple, iterate, and improve!**