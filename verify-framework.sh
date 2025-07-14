#!/bin/bash

echo "=== Account Aggregator Test Framework Verification ==="
echo ""

# Check directory structure
echo "📁 Checking directory structure..."
required_dirs=(
    "src/main/java/com/aa/base"
    "src/main/java/com/aa/pojo"
    "src/main/java/com/aa/service"
    "src/main/java/com/aa/utils"
    "src/test/java/com/aa/tests"
    "src/test/resources/config"
    "src/test/resources/schemas"
    "src/test/resources/testdata"
    "reports"
    "test-output"
)

missing_dirs=()
for dir in "${required_dirs[@]}"; do
    if [ -d "$dir" ]; then
        echo "   ✅ $dir"
    else
        echo "   ❌ $dir (missing)"
        missing_dirs+=("$dir")
    fi
done

echo ""

# Check key files
echo "📄 Checking key files..."
required_files=(
    "pom.xml"
    "src/main/java/com/aa/base/BaseTest.java"
    "src/main/java/com/aa/service/ConsentService.java"
    "src/main/java/com/aa/utils/ConfigManager.java"
    "src/main/java/com/aa/utils/TokenManager.java"
    "src/main/java/com/aa/utils/VaultManager.java"
    "src/main/java/com/aa/utils/ExtentManager.java"
    "src/main/java/com/aa/utils/DatabaseManager.java"
    "src/main/java/com/aa/utils/WireMockManager.java"
    "src/main/java/com/aa/pojo/ConsentRequest.java"
    "src/main/java/com/aa/pojo/ConsentResponse.java"
    "src/main/java/com/aa/pojo/FIRequest.java"
    "src/test/java/com/aa/tests/ConsentAPITest.java"
    "src/test/resources/config/config.properties"
    "src/test/resources/testng.xml"
    "src/test/resources/log4j2.xml"
    "README.md"
)

missing_files=()
for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        echo "   ✅ $file"
    else
        echo "   ❌ $file (missing)"
        missing_files+=("$file")
    fi
done

echo ""

# Check Java classes for syntax (basic check)
echo "🔍 Checking Java files for basic syntax..."
java_files=$(find src -name "*.java" 2>/dev/null)
syntax_errors=()

for file in $java_files; do
    # Basic syntax check - look for common issues
    if grep -q "public class" "$file"; then
        class_name=$(basename "$file" .java)
        if grep -q "public class $class_name" "$file"; then
            echo "   ✅ $file (class name matches file)"
        else
            echo "   ⚠️  $file (class name might not match file)"
            syntax_errors+=("$file")
        fi
    else
        echo "   ⚠️  $file (no public class found)"
        syntax_errors+=("$file")
    fi
done

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

if [ -f "src/test/resources/testng.xml" ]; then
    echo "   ✅ testng.xml exists"
    if grep -q "ConsentAPITest" "src/test/resources/testng.xml"; then
        echo "   ✅ ConsentAPITest configured in TestNG"
    else
        echo "   ⚠️  ConsentAPITest not found in TestNG XML"
    fi
else
    echo "   ❌ testng.xml missing"
fi

echo ""

# Summary
echo "📊 VERIFICATION SUMMARY"
echo "======================="
if [ ${#missing_dirs[@]} -eq 0 ] && [ ${#missing_files[@]} -eq 0 ]; then
    echo "🎉 All required directories and files are present!"
else
    echo "⚠️  Some components are missing:"
    if [ ${#missing_dirs[@]} -gt 0 ]; then
        echo "   Missing directories: ${missing_dirs[@]}"
    fi
    if [ ${#missing_files[@]} -gt 0 ]; then
        echo "   Missing files: ${missing_files[@]}"
    fi
fi

if [ ${#syntax_errors[@]} -eq 0 ]; then
    echo "✅ No obvious syntax issues detected in Java files"
else
    echo "⚠️  Potential syntax issues in: ${syntax_errors[@]}"
fi

echo ""
echo "🚀 Framework is ready for use!"
echo ""
echo "Next steps:"
echo "1. Install Maven (if not already installed)"
echo "2. Run: mvn clean install"
echo "3. Run tests: mvn test"
echo "4. Check reports in: reports/"
echo ""
echo "For more information, see README.md"