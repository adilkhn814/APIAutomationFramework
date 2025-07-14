# Account Aggregator (AA) Test Automation Framework

A comprehensive Java-based test automation framework for Account Aggregator platform APIs, built with industry best practices and modern testing tools.

## 🏗️ Framework Architecture

```
src/
├── main/java/com/aa/
│   ├── base/           # Base test classes and utilities
│   ├── pojo/           # POJOs for request/response objects
│   ├── service/        # API service classes (ConsentService, etc.)
│   └── utils/          # Utility classes (ConfigManager, TokenManager, etc.)
└── test/
    ├── java/com/aa/tests/    # Test classes using TestNG
    └── resources/
        ├── config/           # Configuration files
        ├── schemas/          # JSON schema files for validation
        └── testdata/         # Test data files
```

## 🛠️ Technology Stack

- **Java**: 11+
- **TestNG**: 7.8.0 - Testing framework
- **RestAssured**: 5.4.0 - API testing library
- **Gson**: 2.10.1 - JSON serialization/deserialization
- **ExtentReports**: 5.0.9 - HTML reporting
- **WireMock**: 2.35.1 - Mock external services
- **Log4j2**: 2.21.1 - Logging
- **HikariCP**: 5.0.1 - Database connection pooling
- **Maven**: Build and dependency management

## 🚀 Quick Start

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd account-aggregator-test-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Run smoke tests**
   ```bash
   mvn test -Dtest=ConsentAPITest#testCreateBasicConsent
   ```

## 📋 Framework Features

### ✅ Core Features
- **Singleton Pattern** for DB and token/session management
- **Page Object Model** ready for future UI testing
- **Factory/Service Pattern** for API calls separation
- **Comprehensive Logging** with request/response details
- **Pretty-print** request and response in reports
- **Auto-generated** timestamps and transaction IDs
- **WireMock Integration** for mocking external services
- **ExtentReports** HTML summary after execution
- **Jenkins Compatible** for CI/CD pipelines

### � Utility Classes
- **ConfigManager**: Configuration management with environment override
- **TokenManager**: Authentication token handling with auto-refresh
- **VaultManager**: Credential and secret management (dummy implementation)
- **DatabaseManager**: Database operations with connection pooling
- **WireMockManager**: Mock service management
- **ExtentManager**: Report generation and configuration

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
# Smoke tests only
mvn test -Dsuite=smoke

# Consent API tests
mvn test -Dtest=ConsentAPITest

# With custom configuration
mvn test -Dbase.url=https://staging-api.example.com
```

### Run with TestNG XML
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Environment-specific Execution
```bash
# Staging environment
mvn test -Denvironment=STAGING -Dbase.url=https://staging-api.example.com

# Production environment
mvn test -Denvironment=PROD -Dbase.url=https://api.example.com
```

## 📊 Reports and Logs

### ExtentReports
- **Location**: `reports/extent-report_<timestamp>.html`
- **Features**: 
  - Interactive HTML reports
  - Request/response logging
  - Test execution timeline
  - System information
  - Screenshots and attachments

### Logs
- **Application logs**: `logs/aa-test.log`
- **Error logs**: `logs/aa-test-error.log`
- **Test results**: `logs/test-results.log`

### Opening Reports
```bash
# Open latest report in browser (Linux/Mac)
open reports/extent-report_*.html

# Windows
start reports/extent-report_*.html
```

## ⚙️ Configuration

### Configuration Files
1. **`src/test/resources/config/config.properties`** - Main configuration
2. **`src/test/resources/testng.xml`** - TestNG suite configuration
3. **`src/test/resources/log4j2.xml`** - Logging configuration

### Key Configuration Properties
```properties
# API Configuration
base.url=https://api.account-aggregator.com
auth.url=https://auth.account-aggregator.com
environment=TEST

# Performance Thresholds
performance.response.threshold.ms=5000
performance.total.threshold.ms=10000

# Database (optional)
db.enabled=false
db.url=jdbc:mysql://localhost:3306/aa_test

# WireMock
wiremock.port=8089
wiremock.enabled=true
```

### Environment Variables Override
```bash
export BASE_URL=https://staging-api.example.com
export AUTH_CLIENT_ID=your_client_id
export AUTH_CLIENT_SECRET=your_client_secret
export TEST_ENVIRONMENT=STAGING
```

## 🔒 Security and Credentials

### VaultManager (Dummy Implementation)
The framework includes a dummy `VaultManager` for credential management. In production:

1. Replace with actual vault integration (HashiCorp Vault, AWS Secrets Manager, etc.)
2. Store sensitive data in environment variables
3. Use encrypted configuration files

### Example Credential Setup
```bash
# Set via environment variables
export AUTH_CLIENT_ID="your_actual_client_id"
export AUTH_CLIENT_SECRET="your_actual_client_secret"
export DB_PASSWORD="your_db_password"
```

## 🎯 Endpoints Covered

### Implemented
- ✅ `/Consent` (POST) - Consent creation and management

### Planned
- 🔄 `/FI/request` (POST) - Financial Information request
- 🔄 `/FI/fetch` (POST) - Financial Information fetch
- 🔄 `/callback/Consent` (POST) - Consent callback
- 🔄 `/callback/FIData` (POST) - FI Data callback

## � Test Data Management

### Static Test Data (Current)
```java
// In test classes
private static final String TEST_CUSTOMER_ID = "customer@example.com";
private static final String TEST_FIU_ID = "test-fiu-001";
private static final String TEST_FIP_ID = "test-fip-001";
```

### Future Enhancements
- Excel/CSV data providers
- Database-driven test data
- Dynamic test data generation
- Data masking and privacy

## 🔧 Adding New Tests

### 1. Create POJO Classes
```java
// src/main/java/com/aa/pojo/NewRequest.java
public class NewRequest {
    @SerializedName("field1")
    private String field1;
    // ... getters, setters, constructors
}
```

### 2. Create Service Class
```java
// src/main/java/com/aa/service/NewService.java
public class NewService {
    public Response createNewRequest(NewRequest request) {
        // Implementation
    }
}
```

### 3. Create Test Class
```java
// src/test/java/com/aa/tests/NewAPITest.java
public class NewAPITest extends BaseTest {
    @Test
    public void testNewAPI() {
        // Test implementation
    }
}
```

## 🚀 CI/CD Integration

### Jenkins Pipeline Example
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Denvironment=STAGING'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'reports',
                        reportFiles: 'extent-report*.html',
                        reportName: 'Test Report'
                    ])
                }
            }
        }
    }
}
```

### GitHub Actions Example
```yaml
name: API Tests
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
        distribution: 'adopt'
    - name: Run tests
      run: mvn clean test
    - name: Upload reports
      uses: actions/upload-artifact@v2
      with:
        name: test-reports
        path: reports/
```

## 🐛 Troubleshooting

### Common Issues

1. **Tests failing with authentication errors**
   ```bash
   # Check token configuration
   tail -f logs/aa-test.log | grep -i token
   ```

2. **WireMock port conflicts**
   ```bash
   # Change port in config.properties
   wiremock.port=8090
   ```

3. **Database connection issues**
   ```bash
   # Disable database if not needed
   db.enabled=false
   ```

4. **Report generation fails**
   ```bash
   # Create reports directory
   mkdir -p reports
   ```

### Debug Mode
```bash
# Enable debug logging
mvn test -Dlog.level=DEBUG

# Enable RestAssured logging
mvn test -Dio.restassured.filter.log=ALL
```

## 📈 Performance Testing

### Response Time Thresholds
- API response time: < 5 seconds
- Total test execution: < 10 seconds

### Load Testing (Future)
- Concurrent user simulation
- Stress testing endpoints
- Performance regression detection

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

### Code Standards
- Follow Java naming conventions
- Add comprehensive JavaDoc comments
- Maintain test coverage > 80%
- Use meaningful assertion messages

## � Additional Resources

- [Account Aggregator Specification](https://api.rebit.org.in/)
- [RestAssured Documentation](https://rest-assured.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [ExtentReports Documentation](https://extentreports.com/)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For questions and support:
- Create an issue in the repository
- Contact the QA team
- Check the troubleshooting guide above

---

**Happy Testing! 🎉**


