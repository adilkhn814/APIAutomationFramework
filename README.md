🚀 API Automation Framework
                        
This is a Java-based REST API automation framework built using RestAssured and TestNG.

It supports automated testing of core user flows such as login, signup, and profile update, along with robust data handling and reporting mechanisms.

✨ Features

    🔧 Configurable Test Parameters: Easily manage environment variables via config.properties.
  
    🔄 CSV-Driven Testing: Supports data-driven testing using OpenCSV and external .csv files.
  
    🤖 Random Data Generation: Uses JavaFaker to generate dynamic test data (emails, names, numbers).
  
    🛡️ Reusable POJOs & Service Classes: Clean separation between test data, test logic, and services.
  
    ✅ Built-in Assertions: Validates responses for status codes, tokens, roles, and more.
  
    📦 Structured Codebase: Modular package structure with utilities, listeners, and filters.
  
    ⚙️ CI/CD Integrated: GitHub Actions integrated for automated test runs on push/PR.


  

   <img width="787" alt="Screenshot 2025-06-04 at 6 37 57 AM" src="https://github.com/user-attachments/assets/747f6d40-60e0-4f1c-9f60-e6a92d54c1b8" />




⚙️ Setup Instructions

  1. Clone the Repository
     
<img width="584" alt="Screenshot 2025-06-04 at 6 38 35 AM" src="https://github.com/user-attachments/assets/a08c9b90-7f20-41df-919b-80e9ed8b71bb" />



2. Configure config.properties

        Create or update the file at:
  
        src/test/resources/config.properties

  
<img width="589" alt="Screenshot 2025-06-04 at 6 39 01 AM" src="https://github.com/user-attachments/assets/80d16c60-8e45-4b94-9f77-ff32ed28314c" />

3. Run Tests

       Using your IDE (e.g. IntelliJ, Eclipse):
   
         Right-click the test class or suite file and select Run.
         Or via command line (using Maven):


 <img width="609" alt="Screenshot 2025-06-04 at 6 39 39 AM" src="https://github.com/user-attachments/assets/64ee0eb6-cb47-413b-8ee8-852e4f6df12a" />




🔧 Usage

    Accessing Configuration

<img width="604" alt="Screenshot 2025-06-04 at 6 40 01 AM" src="https://github.com/user-attachments/assets/e7ef2e91-2ff0-4d7d-afa0-c3a9a833fa9a" />




Generating Random Test Data



<img width="611" alt="Screenshot 2025-06-04 at 6 40 27 AM" src="https://github.com/user-attachments/assets/9490c603-6631-4eed-a771-c96cb37ad806" />



Reading CSV Data


<img width="675" alt="Screenshot 2025-06-04 at 6 41 07 AM" src="https://github.com/user-attachments/assets/c555a28d-57b3-48b9-b8f6-45cf67e51a13" />

✅ CI Integration

    This project is integrated with GitHub Actions to automatically run tests on:

    Every push to the main branch

    Every pull request

    A sample .github/workflows/api-tests.yml (not shown here) executes Maven commands and runs TestNG tests headlessly.


🧩 Dependencies

    rest-assured

    testng

    opencsv

    javafaker

    json-schema-validator

    commons-io

    log4j-api

    log4j-core

    jackson-databind

    extentreports

    javax.mail

    mysql-connector-java

    poi-ooxml



👤 Author

    Adil Khan

    📧 Email: adilkhn814@gmail.com

    💼 LinkedIn: linkedin.com/in/your-profile](https://www.linkedin.com/in/adil-khan-869360137/)


