package com.aa.bdd.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber Test Runner class for Account Aggregator BDD Tests
 * Integrates Cucumber with TestNG for execution
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
            "com.aa.bdd.stepdefinitions",
            "com.aa.bdd.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-reports/cucumber-html-report",
            "json:target/cucumber-reports/cucumber.json",
            "junit:target/cucumber-reports/cucumber.xml",
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
            "timeline:target/cucumber-reports/timeline"
        },
        tags = "@smoke or @regression",
        monochrome = true,
        publish = false,
        dryRun = false
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    /**
     * Enable parallel execution of scenarios
     * Override this method to configure parallel execution
     */
    @Override
    @DataProvider(parallel = false) // Set to true for parallel execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Additional Test Runners for different test categories
 */

/**
 * Smoke Test Runner - Runs only smoke tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
            "com.aa.bdd.stepdefinitions",
            "com.aa.bdd.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-reports/smoke-html-report",
            "json:target/cucumber-reports/smoke.json",
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        tags = "@smoke",
        monochrome = true,
        publish = false
)
class SmokeTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Regression Test Runner - Runs all regression tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
            "com.aa.bdd.stepdefinitions",
            "com.aa.bdd.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-reports/regression-html-report",
            "json:target/cucumber-reports/regression.json",
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        tags = "@regression",
        monochrome = true,
        publish = false
)
class RegressionTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Consent Feature Test Runner - Runs only consent-related tests
 */
@CucumberOptions(
        features = "src/test/resources/features/consent_management.feature",
        glue = {
            "com.aa.bdd.stepdefinitions",
            "com.aa.bdd.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-reports/consent-html-report",
            "json:target/cucumber-reports/consent.json",
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        tags = "@consent",
        monochrome = true,
        publish = false
)
class ConsentTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Financial Information Test Runner - Runs only FI-related tests
 */
@CucumberOptions(
        features = "src/test/resources/features/financial_information.feature",
        glue = {
            "com.aa.bdd.stepdefinitions",
            "com.aa.bdd.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-reports/fi-html-report",
            "json:target/cucumber-reports/fi.json",
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        tags = "@fi",
        monochrome = true,
        publish = false
)
class FinancialInformationTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

/**
 * Performance Test Runner - Runs only performance tests
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
            "com.aa.bdd.stepdefinitions",
            "com.aa.bdd.hooks"
        },
        plugin = {
            "pretty",
            "html:target/cucumber-reports/performance-html-report",
            "json:target/cucumber-reports/performance.json",
            "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        tags = "@performance",
        monochrome = true,
        publish = false
)
class PerformanceTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}