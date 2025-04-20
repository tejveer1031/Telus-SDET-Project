pipeline {
	agent any

    tools {
        jdk 'jdk21'
        maven 'maven3'
    }

    stages {
		stage('Checkout') {
			steps {
				// Pull the code from GitHub
                checkout scm
            }
        }

        stage('Build & Test') {
			steps {
				// Clean, compile, and run TestNG/Cucumber tests via testng.xml
                sh 'mvn clean verify -DsuiteXmlFile=testng.xml'
            }
            post {
				always {
					// Publish test results (TestNG XML is JUnit-compatible)
                    junit '**/target/surefire-reports/*.xml'
                    // Generate Allure report from results directory
                    allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
                }
            }
        }

        stage('Archive Artifacts') {
			steps {
				// Save the built JAR for downloads
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                // Save the Allure HTML report as well
                archiveArtifacts artifacts: 'allure-report/**/*.*', fingerprint: true
            }
        }
    }

    post {
		// Always clean up the workspace
        always {
			cleanWs()
        }
    }
}
