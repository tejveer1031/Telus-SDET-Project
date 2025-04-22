pipeline {
	agent any

    tools {
		jdk 'jdk21'
        maven 'Maven3'
    }

    environment {
		DOCKER_IMAGE = "tejveer001/telus-sdet-project:latest"
        ALLURE_RESULTS = "${WORKSPACE}/allure-results"
        GITHUB_REPO = "https://github.com/tejveer1031/Telus-SDET-Project"
        BRANCH = "main"
    }

    stages {
		stage('Checkout') {
			steps {
				checkout scm
            }
        }

        stage('Docker Build & Run') {
			steps {
				withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
					script {
						// Build and run tests in container
                        sh """
                            docker build \
                                --build-arg GITHUB_REPO=${GITHUB_REPO} \
                                --build-arg BRANCH=${BRANCH} \
                                -t ${DOCKER_IMAGE} .

                            docker run --rm \
                                -v ${WORKSPACE}/allure-results:/app/target/allure-results \
                                ${DOCKER_IMAGE}
                        """
                    }
                }
            }
        }

        stage('Allure Report') {
			steps {
				allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']], // Match mounted volume
                    reportBuildPolicy: 'ALWAYS',
                    commandline: 'allure'
                ])
            }
        }

        stage('Push Docker Image') {
			when {
				branch 'main'
            }
            steps {
				withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
					sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
		always {
			sh 'docker logout'
            cleanWs()
        }
        success {
			emailext(
                subject: "✅ PASSED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """|Allure Report: ${env.BUILD_URL}allure/
                         |Build URL: ${env.BUILD_URL}""".stripMargin(),
                to: 'Tejihayer.92@gmail.com'
            )
        }
        failure {
			emailext(
                subject: "❌ FAILED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """|Allure Report: ${env.BUILD_URL}allure/
                         |Build URL: ${env.BUILD_URL}
                         |Console Output: ${env.BUILD_URL}console""".stripMargin(),
                to: 'Tejihayer.92@gmail.com'
            )
        }
    }
}