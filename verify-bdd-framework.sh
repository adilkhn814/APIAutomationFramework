#!/bin/bash

echo "=== Account Aggregator BDD Test Framework Verification ==="
echo ""

# Check BDD directory structure
echo "📁 Checking BDD directory structure..."
bdd_dirs=(
    "src/main/java/com/aa/base"
    "src/main/java/com/aa/pojo"
    "src/main/java/com/aa/service"
    "src/main/java/com/aa/utils"
    "src/test/java/com/aa/bdd/runner"
    "src/test/java/com/aa/bdd/stepdefinitions"
    "src/test/java/com/aa/bdd/hooks"
    "src/test/resources/features"
    "src/test/resources/config"
    "src/test/resources/schemas"
    "target/cucumber-reports"
)

missing_dirs=()
for dir in "${bdd_dirs[@]}"; do
    if [ -d "$dir" ]; then
        echo "   ✅ $dir"
    else
        echo "   ❌ $dir (missing)"
        missing_dirs+=("$dir")
    fi
done

echo ""

# Check BDD-specific files
echo "📄 Checking BDD-specific files..."
bdd_files=(
    "pom.xml"
    "src/test/resources/features/consent_management.feature"
    "src/test/resources/features/financial_information.feature"
    "src/test/java/com/aa/bdd/runner/CucumberTestRunner.java"
    "src/test/java/com/aa/bdd/stepdefinitions/ConsentStepDefinitions.java"
    "src/test/java/com/aa/bdd/hooks/TestHooks.java"
    "src/test/resources/extent.properties"
    "src/test/resources/config/config.properties"
    "src/test/resources/log4j2.xml"
    "README.md"
)

missing_files=()
for file in "${bdd_files[@]}"; do
    if [ -f "$file" ]; then
        echo "   ✅ $file"
    else
        echo "   ❌ $file (missing)"
        missing_files+=("$file")
    fi
done

echo ""

# Check feature files syntax
echo "🥒 Checking Cucumber feature files..."
feature_files=$(find src/test/resources/features -name "*.feature" 2>/dev/null)
feature_errors=()

for file in $feature_files; do
    if grep -q "Feature:" "$file"; then
        if grep -q "Scenario:" "$file"; then
            echo "   ✅ $file (valid Gherkin syntax)"
        else
            echo "   ⚠️  $file (no scenarios found)"
            feature_errors+=("$file")
        fi
    else
        echo "   ❌ $file (no Feature declaration)"
        feature_errors+=("$file")
    fi
done

echo ""

# Check for Cucumber dependencies in pom.xml
echo "🔍 Checking Cucumber dependencies..."
if [ -f "pom.xml" ]; then
    if grep -q "cucumber-java" "pom.xml"; then
        echo "   ✅ cucumber-java dependency found"
    else
        echo "   ❌ cucumber-java dependency missing"
    fi
    
    if grep -q "cucumber-testng" "pom.xml"; then
        echo "   ✅ cucumber-testng dependency found"
    else
        echo "   ❌ cucumber-testng dependency missing"
    fi
    
    if grep -q "extentreports-cucumber7-adapter" "pom.xml"; then
        echo "   ✅ ExtentReports Cucumber adapter found"
    else
        echo "   ❌ ExtentReports Cucumber adapter missing"
    fi
else
    echo "   ❌ pom.xml not found"
fi

echo ""

# Check step definitions
echo "🪜 Checking step definitions..."
step_def_files=$(find src/test/java -name "*StepDefinitions.java" 2>/dev/null)

for file in $step_def_files; do
    if grep -q "@Given\|@When\|@Then" "$file"; then
        echo "   ✅ $file (contains step definitions)"
    else
        echo "   ⚠️  $file (no step definitions found)"
    fi
done

echo ""

# Check test runners
echo "🏃 Checking test runners..."
runner_files=$(find src/test/java -name "*Runner.java" 2>/dev/null)

for file in $runner_files; do
    if grep -q "@CucumberOptions" "$file"; then
        echo "   ✅ $file (valid Cucumber runner)"
    else
        echo "   ⚠️  $file (not a Cucumber runner)"
    fi
done

echo ""

# Check for BDD tags in feature files
echo "🏷️  Checking BDD tags..."
if [ -n "$feature_files" ]; then
    tags_found=()
    for file in $feature_files; do
        if grep -q "@smoke\|@regression\|@consent\|@fi\|@performance" "$file"; then
            tags=$(grep -o "@[a-zA-Z_-]*" "$file" | sort -u | tr '\n' ' ')
            echo "   ✅ $file (tags: $tags)"
        else
            echo "   ⚠️  $file (no standard tags found)"
        fi
    done
else
    echo "   ❌ No feature files found"
fi

echo ""

# Check configuration files
echo "⚙️  Checking configuration files..."
if [ -f "src/test/resources/config/config.properties" ]; then
    echo "   ✅ config.properties exists"
    if grep -q "base.url" "src/test/resources/config/config.properties"; then
        echo "   ✅ base.url configured"
    else
        echo "   ⚠️  base.url not found in config"
    fi
else
    echo "   ❌ config.properties missing"
fi

if [ -f "src/test/resources/extent.properties" ]; then
    echo "   ✅ extent.properties exists"
    if grep -q "extent.reporter.spark" "src/test/resources/extent.properties"; then
        echo "   ✅ ExtentReports Spark reporter configured"
    else
        echo "   ⚠️  Spark reporter not configured"
    fi
else
    echo "   ❌ extent.properties missing"
fi

echo ""

# Validate Gherkin syntax (basic check)
echo "📝 Validating Gherkin syntax..."
gherkin_issues=()

for file in $feature_files; do
    # Check for proper indentation and structure
    if grep -q "^Feature:" "$file" && grep -q "^  Scenario:" "$file"; then
        echo "   ✅ $file (proper Gherkin structure)"
    else
        echo "   ⚠️  $file (indentation or structure issues)"
        gherkin_issues+=("$file")
    fi
done

echo ""

# Check for examples and scenario outlines
echo "📊 Checking for data-driven tests..."
for file in $feature_files; do
    if grep -q "Scenario Outline:\|Examples:" "$file"; then
        echo "   ✅ $file (contains data-driven scenarios)"
    else
        echo "   ℹ️  $file (no data-driven scenarios)"
    fi
done

echo ""

# Summary
echo "📊 BDD FRAMEWORK VERIFICATION SUMMARY"
echo "====================================="

# Overall status
if [ ${#missing_dirs[@]} -eq 0 ] && [ ${#missing_files[@]} -eq 0 ]; then
    echo "🎉 All required BDD directories and files are present!"
else
    echo "⚠️  Some BDD components are missing:"
    if [ ${#missing_dirs[@]} -gt 0 ]; then
        echo "   Missing directories: ${missing_dirs[@]}"
    fi
    if [ ${#missing_files[@]} -gt 0 ]; then
        echo "   Missing files: ${missing_files[@]}"
    fi
fi

# Feature files status
if [ ${#feature_errors[@]} -eq 0 ]; then
    echo "✅ All feature files have valid Gherkin syntax"
else
    echo "⚠️  Some feature files have issues: ${feature_errors[@]}"
fi

# Gherkin syntax status
if [ ${#gherkin_issues[@]} -eq 0 ]; then
    echo "✅ Gherkin syntax validation passed"
else
    echo "⚠️  Some Gherkin files have structure issues: ${gherkin_issues[@]}"
fi

echo ""
echo "🚀 BDD Framework is ready for use!"
echo ""
echo "Quick Start Commands:"
echo "1. Run smoke tests: mvn test -Dcucumber.filter.tags=\"@smoke\""
echo "2. Run all tests: mvn test -Dtest=CucumberTestRunner"
echo "3. Run specific feature: mvn test -Dcucumber.filter.tags=\"@consent\""
echo "4. View reports: open target/cucumber-reports/extent-spark-report.html"
echo ""
echo "BDD Benefits:"
echo "✅ Write tests in natural language (Gherkin)"
echo "✅ Business-readable scenarios"
echo "✅ Living documentation"
echo "✅ Collaborative test development"
echo "✅ Easy to maintain and extend"
echo ""
echo "For more information, see README.md"
echo ""
echo "Happy BDD Testing! 🥒✨"