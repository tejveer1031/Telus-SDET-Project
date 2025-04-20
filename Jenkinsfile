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
			// Optional: Send notifications (Slack/Email)
            slackSend channel: '#your-channel', message: "Build ${BUILD_URL} succeeded! 🚀"
        }
    }
}