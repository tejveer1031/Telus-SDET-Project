pipeline {
	agent any

    tools {
		jdk 'jdk21'
        maven 'Maven3'
    }

    stages {
		stage('Checkout') {
			steps {
				checkout scm  // Pulls code from the configured SCM (GitHub)
            }
        }

        stage('Build & Test') {
			steps {
				bat 'mvn clean verify -DsuiteXmlFile=testng.xml'  // Run TestNG/Cucumber tests
            }
        }

       stage('Allure Report') {
			steps {
				allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'allure-results']],
                    reportBuildPolicy: 'ALWAYS',
                    commandline: 'allure'
                ])
            }
        }

        stage('Archive Artifacts') {
			steps {
				archiveArtifacts artifacts: 'target/*.jar', fingerprint: true  // Save JAR
                archiveArtifacts artifacts: 'allure-report/**/*', fingerprint: true  // Save HTML report
            }
        }
    }

    post {
		always {
			cleanWs()  // Clean workspace after all stages
        }
        success {
			emailext(
                subject: "Passed: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: "Passed",
                to: 'Tejihayer.92@gmail.com'
            )
        }
        failure {
			emailext(
                subject: "❌ FAILED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: "Check failed build: ${env.BUILD_URL}",
                to: 'Tejihayer.92@gmail.com'
            )
        }
    }
}