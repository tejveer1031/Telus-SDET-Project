# 🚀 TELUS SDET Automation Framework

[![Java](https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk)](https://java.com)
[![Selenium](https://img.shields.io/badge/Selenium-4.1.4-%43B02A?logo=selenium)](https://selenium.dev)
[![TestNG](https://img.shields.io/badge/TestNG-7.5-%23FF6A00)](https://testng.org)
[![Docker](https://img.shields.io/badge/Docker-24.0-%232496ED?logo=docker)](https://docker.com)
[![Jenkins](https://img.shields.io/badge/Jenkins-2.414-%23D24939?logo=jenkins)](http://your-ngrok-url.ngrok.io/job/Telus-SDET-Pipeline/build?token=YOUR_TOKEN)


An enterprise-grade test automation framework implementing Behavior-Driven Development (BDD) with the Page Object Model design pattern. The CI/CD pipeline enables seamless integration with cross-browser testing and comprehensive reporting.

---

## ☁️ Cloud Deployment Architecture

[![AWS EC2](https://img.shields.io/badge/AWS_EC2-Instance-%23FF9900?logo=amazon-aws)](https://aws.amazon.com)
[![Dockerized CI/CD](https://img.shields.io/badge/Pipeline-100%25_Containerized-%232496ED?logo=docker)](https://docker.com)
[![Jenkins](https://img.shields.io/badge/CI/CD-Jenkins-%23D24939?logo=jenkins)](http://your-jenkins-url:8080)

### Infrastructure Highlights

- **AWS EC2 Ubuntu Server**: Scalable t3.xlarge instance with automated snapshot backups.
- **Docker Swarm**: Cluster management for parallel cross-browser test execution.
- **Jenkins Pipeline**: Multibranch declarative pipelines with pipeline-as-code.
- **Security**: IAM role-based access, VPC isolation, HTTPS via Let's Encrypt.

```text
+-------------------+     +-----------------+     +---------------+
|   GitHub Repo     |     | Jenkins (EC2)   |     | Docker Swarm  |
|                   +----->                 +----->               |
| Code Commit/PR    |     | - Build Trigger |     | - Chrome      |
+-------------------+     | - Test Execution|     | - Firefox     |
                          | - Allure Reports|     | - Headless    |
                          +-----------------+     +---------------+      
```
## 🌟 Key Features
- 🧩 **Modular Architecture**: Page Object Model with reusable components
- ⚡ **Parallel Execution**: TestNG-powered multi-threaded runs (up to 4x faster)
- 📊 **Smart Reporting**: Allure + Cucumber HTML interactive reports with screenshots
- 🐳 **One-Click Execution**: Dockerized testing environment
- 🤖 **CI/CD Pipeline**: Jenkins multibranch with Slack/Email notifications
- 🌐 **Cross-Browser Support**: Chrome & Firefox (headless/headed modes)

## 🛠 Tech Stack
| Category        | Technologies                                                                 |
|-----------------|-----------------------------------------------------------------------------|
| **Core**        | Java 21 · Maven 3.8.6 · TestNG 7.5 · Selenium 4.1.4                        |
| **BDD**         | Cucumber 7.15 · Gherkin · JUnit 5                                          |
| **Reporting**   | Allure 2.24 · Extent Reports · CI Dashboard Integration                    |
| **CI/CD**       | Jenkins · Docker · GitHub Actions · Ngrok                                  |
| **Utilities**   | WebDriverManager · Lombok · Log4j2 · Faker                                 |

## 🚀 Quick Start

### Prerequisites
- Java 21+ ([Adoptium Temurin](https://adoptium.net/))
- Docker Desktop ([Get Docker](https://www.docker.com/products/docker-desktop/))

### Run via Docker (Recommended)
```bash
1.Open Terminal/CMD
Start Docker Desktop first (if using Windows/Mac).

2. Run the Docker Image
docker run -it --rm tejveer001/telus-sdet-project:latest

```
##Project Structure
```
telus-sdet-project/
├── src/
│   ├── test/
│   │   └── java/
│   │       └── com/telus/automation/
│   │           ├── pages/             # Page Object Models
│   │           ├── runners/           # Test Runners
│   │           ├── enums/             # Enums for constants
│   │           ├── hooks/             # Hooks for setup/teardown
│   │           ├── tests/             # Test classes
│   │           └── stepdefinitions/   # Cucumber Step Definitions
│   └── resources/
│       ├── features/                  # Cucumber Feature Files
│       └── config.properties          # Configuration Properties
├── reports/                           # Generated Test Reports
├── pom.xml                            # Maven Configuration File
├── Jenkinsfile                        # Jenkins Pipeline Configuration
├── Dockerfile                         # Docker Configuration
├── trigger_jenkins.sh                 # Script to trigger Jenkins builds
└── README.md                          # Project Documentation
```


📞 Contact
For any inquiries or support, please contact hayertejveersingh@gmail.com.

