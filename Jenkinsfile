pipeline {
	agent any

    tools {
		jdk 'jdk_21_latest'
        maven 'Maven 3 (latest)'
    }

    stages {
		stage('Checkout') {
			steps {
				checkout scm  // Pulls code from the configured SCM (GitHub)
            }
        }

        stage('Build & Test') {
			steps {
				sh 'mvn clean verify -DsuiteXmlFile=testng.xml'  // Run TestNG/Cucumber tests
            }
            post {
				always {
					junit '**/target/surefire-reports/*.xml'   // Publish JUnit-compatible results
                    allure(
                        includeProperties: false,
                        jdk: '',
                        results: [[path: 'allure-results']]  // Raw Allure results directory
                    )
                }
            }
        }

        // Add this stage to generate the HTML report
        stage('Generate Allure Report') {
			steps {
				allure commandline: 'allure', generate: 'allure-results', report: 'allure-report'
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