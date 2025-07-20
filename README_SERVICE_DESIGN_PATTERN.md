# 🚀 Service Design Pattern Implementation

Your API automation framework has been refactored to follow the **Service Design Pattern**, providing better separation of concerns, dependency injection, and maintainable code architecture.

## 🎯 Service Design Pattern Benefits

✅ **Separation of Concerns**: Each service has a single responsibility  
✅ **Dependency Injection**: Services are injected rather than directly instantiated  
✅ **Testability**: Easy to mock services for unit testing  
✅ **Maintainability**: Changes in one service don't affect others  
✅ **Reusability**: Services can be composed and reused across different contexts  
✅ **Configuration Management**: Centralized configuration handling  

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Service Design Pattern                   │
├─────────────────────────────────────────────────────────────┤
│  Business Layer                                             │
│  ├── UserManagementBusinessService                          │
│  └── [Other Business Services]                              │
├─────────────────────────────────────────────────────────────┤
│  Service Layer                                              │
│  ├── IAuthService → AuthServiceImpl                         │
│  ├── IUserProfileService → UserProfileServiceImpl          │
│  ├── IHttpClientService → HttpClientServiceImpl            │
│  ├── IConfigurationService → ConfigurationServiceImpl      │
│  └── IValidationService → ValidationServiceImpl            │
├─────────────────────────────────────────────────────────────┤
│  Core Layer                                                 │
│  ├── ServiceLocator (Dependency Injection)                 │
│  └── BaseTest (Service Integration for Tests)              │
├─────────────────────────────────────────────────────────────┤
│  Infrastructure Layer                                       │
│  ├── REST Client (RestAssured)                             │
│  ├── Configuration (Properties)                            │
│  └── Logging & Filtering                                   │
└─────────────────────────────────────────────────────────────┘
```

## 📁 New Package Structure

```
src/main/java/com/api/
├── core/
│   └── ServiceLocator.java              # Dependency injection container
├── services/
│   ├── interfaces/                      # Service contracts
│   │   ├── IAuthService.java
│   │   ├── IUserProfileService.java
│   │   ├── IHttpClientService.java
│   │   ├── IConfigurationService.java
│   │   └── IValidationService.java
│   ├── impl/                           # Service implementations
│   │   ├── AuthServiceImpl.java
│   │   ├── UserProfileServiceImpl.java
│   │   ├── HttpClientServiceImpl.java
│   │   ├── ConfigurationServiceImpl.java
│   │   └── ValidationServiceImpl.java
│   └── business/                       # Business logic services
│       └── UserManagementBusinessService.java

src/test/java/com/api/
├── base/
│   └── BaseTest.java                   # Service-enabled base test class
└── test/enhanced/
    └── LoginTestWithServices.java     # Example using service pattern
```

## 🔧 Key Components

### 1. Service Locator (Dependency Injection)
```java
// Get service instances
ServiceLocator serviceLocator = ServiceLocator.getInstance();
IAuthService authService = serviceLocator.getService(IAuthService.class);
```

### 2. Configuration Service
```java
// Centralized configuration management
IConfigurationService configService = serviceLocator.getService(IConfigurationService.class);
String baseUrl = configService.getBaseUrl();
configService.setProperty("custom.property", "value");
```

### 3. HTTP Client Service  
```java
// Abstracted HTTP operations
IHttpClientService httpClient = serviceLocator.getService(IHttpClientService.class);
httpClient.setAuthToken(token);
Response response = httpClient.post("/api/endpoint", payload);
```

### 4. Business Services
```java
// High-level business operations
UserManagementBusinessService userService = new UserManagementBusinessService();
UserLoginResult result = userService.loginAndGetProfile(username, password);
```

## 🚦 How to Use the Service Design Pattern

### In Your Tests (Recommended Approach)

```java
@Listeners(com.api.listeners.TestListener.class)
public class MyApiTest extends BaseTest {
    
    @Test
    public void testUserWorkflow() {
        // Services are automatically injected via BaseTest
        
        // 1. Login using auth service
        LoginRequest loginRequest = new LoginRequest.Builder()
            .username(configService.getUsername())
            .password(configService.getPassword())
            .build();
        
        LoginResponse loginResponse = authService.login(loginRequest);
        
        // 2. Get profile using user profile service
        GetProfileResponse profile = userProfileService.getProfile(loginResponse.getToken());
        
        // 3. Or use business service for complex workflows
        UserLoginResult result = userManagementService.loginAndGetProfile(null, null);
        
        // Assertions...
    }
}
```

### Direct Service Usage
```java
// Get service locator
ServiceLocator serviceLocator = ServiceLocator.getInstance();

// Get specific services
IAuthService authService = serviceLocator.getService(IAuthService.class);
IConfigurationService configService = serviceLocator.getService(IConfigurationService.class);

// Use services
LoginResponse response = authService.login(loginRequest);
```

## 🧪 Testing with Service Design Pattern

### 1. **Service Injection in Tests**
```java
public class MyTest extends BaseTest {
    // Services automatically injected via BaseTest
    // authService, userProfileService, configService, etc. available
}
```

### 2. **Mock Services for Unit Testing**
```java
@Test
public void testWithMockService() {
    // Replace service with mock for testing
    IAuthService mockAuthService = Mockito.mock(IAuthService.class);
    replaceService(IAuthService.class, mockAuthService);
    
    // Configure mock behavior
    when(mockAuthService.login(any())).thenReturn(mockLoginResponse);
    
    // Run test with mock
}
```

### 3. **Business Service Testing**
```java
@Test
public void testComplexWorkflow() {
    // Use business service for complex operations
    UserRegistrationResult result = userManagementService.registerAndSetupUser(signUpRequest);
    
    // Verify all steps completed successfully
    Assert.assertEquals(result.getSignUpResponse().getStatusCode(), 201);
    Assert.assertNotNull(result.getLoginResponse().getToken());
    Assert.assertNotNull(result.getProfileResponse());
}
```

## ⚙️ Configuration Management

The service design pattern includes enhanced configuration management:

```properties
# src/main/resources/config.properties

# API Configuration
base.url=http://64.227.160.186:8080
username=testuser
password=password123

# HTTP Client Configuration  
request.timeout=30000
logging.enabled=true

# Test Data Configuration
generate.random.data=true
csv.data.path=src/test/resources/testdata

# Environment Configuration
environment=test
api.version=v1
```

## 🔄 Service Composition Examples

### Authentication + Profile Workflow
```java
// Business service composes multiple services
public UserLoginResult loginAndGetProfile(String username, String password) {
    // Step 1: Login (AuthService)
    LoginResponse loginResponse = authService.login(loginRequest);
    
    // Step 2: Get Profile (UserProfileService)  
    GetProfileResponse profileResponse = userProfileService.getProfile(loginResponse.getToken());
    
    return new UserLoginResult(loginResponse, profileResponse);
}
```

### Registration + Setup Workflow
```java
public UserRegistrationResult registerAndSetupUser(SignUpRequest signUpRequest) {
    // Step 1: Sign up user
    Response signUpResponse = authService.signUp(signUpRequest);
    
    // Step 2: Login with new credentials
    LoginResponse loginResponse = authService.login(loginRequest);
    
    // Step 3: Get initial profile
    GetProfileResponse profileResponse = userProfileService.getProfile(loginResponse.getToken());
    
    return new UserRegistrationResult(signUpResponse, loginResponse, profileResponse);
}
```

## 🛠️ Advanced Features

### 1. **Token Management**
```java
// Automatic token handling
authService.login(loginRequest); // Stores valid tokens
boolean isValid = authService.validateToken(token);
authService.invalidateToken(token); // Cleanup
```

### 2. **Validation Service**
```java
IValidationService validationService = serviceLocator.getService(IValidationService.class);
ValidationResult result = validationService.validateLoginRequestWithDetails(loginRequest);

if (!result.isValid()) {
    System.out.println("Validation failed: " + result.getErrorMessage());
}
```

### 3. **HTTP Client Management**
```java
// Automatic header management
httpClientService.setAuthToken(token);
httpClientService.setHeader("Custom-Header", "value");

// Request with all configured headers
Response response = httpClientService.post("/api/endpoint", payload);

// Clean reset
httpClientService.reset();
```

## 🎯 Migration from Old to New Pattern

### Old Approach ❌
```java
// Direct service instantiation
AuthService authService = new AuthService();
UserProfileManagementService profileService = new UserProfileManagementService();

// Manual configuration handling
String baseUrl = System.getProperty("base.url");
```

### New Approach ✅
```java
// Service injection via ServiceLocator
public class MyTest extends BaseTest {
    // Services automatically available: authService, userProfileService, configService
    
    @Test
    public void myTest() {
        // Use injected services
        LoginResponse response = authService.login(request);
        
        // Or use business services
        UserLoginResult result = userManagementService.loginAndGetProfile(null, null);
    }
}
```

## 🚀 Benefits Achieved

1. **Cleaner Tests**: No manual service instantiation
2. **Better Testability**: Easy mocking and service replacement  
3. **Centralized Configuration**: All config in one place
4. **Error Handling**: Consistent validation and error reporting
5. **Service Composition**: Complex workflows through business services
6. **Maintainability**: Changes isolated to specific services
7. **Reusability**: Services can be reused across different test scenarios

## 🔄 Running Tests

```bash
# Run with Maven
mvn test -Dsuite=suite

# Run specific test class
mvn test -Dtest=LoginTestWithServices

# Run with custom configuration
mvn test -Dbase.url=http://localhost:8080 -Dusername=customuser
```

Your framework now follows enterprise-level service design patterns! 🎉