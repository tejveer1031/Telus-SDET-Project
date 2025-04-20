# TELUS SDET Automation Project

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://java.com)
[![Maven](https://img.shields.io/badge/Maven-3.8.6-blue.svg)](https://maven.apache.org)
[![Selenium](https://img.shields.io/badge/Selenium-4.1.4-orange.svg)](https://selenium.dev)
[![TestNG](https://img.shields.io/badge/TestNG-7.5-red.svg)](https://testng.org)
[![Jenkins](https://img.shields.io/badge/Run_Remotely-Jenkins-blue?logo=jenkins)](http://your-ngrok-url.ngrok.io/job/Telus-SDET-Pipeline/build?token=YOUR_TOKEN)

BDD test automation framework implementing **Page Object Model (POM)** with Java, Selenium, TestNG, and Cucumber.  
CI/CD powered by Jenkins Multibranch Pipeline with automated reporting and remote execution.


## 🚀 Features
- **Test Runner:** TestNG with parallel execution
- **Reporting:** Allure + Cucumber HTML Reports
- **CI/CD:** Automatic branch detection & builds
- **Design Pattern:** Page Object Model (POM)
- **Remote Execution:** One-click test triggering

## 🛠 Tech Stack
| Component       | Version                           |
|-----------------|-----------------------------------|
| Java            | 21                                |
| Selenium        | 4.1.4                             |
| TestNG          | 7.5                               |
| Maven           | 3.8.6                             |
| Jenkins         | 2.414 (with Ngrok tunneling)      |
| Allure          | 2.24.0                            |

## ⚡ Quick Start

### 1. Run Locally
```bash
git clone https://github.com/tejveer1031/Telus-SDET-Project.git
cd Telus-SDET-Project
mvn clean test