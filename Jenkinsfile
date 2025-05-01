pipeline {
	agent {
		docker {
			image 'maven:3.9.6-eclipse-temurin-21'
            args '-v /var/run/docker.sock:/var/run/docker.sock -u root --memory=1g --cpus=0.8'
        }
    }

    tools {
		jdk 'jdk21'
        maven 'Maven3'
    }

    environment {
		DOCKER_IMAGE = "tejveer001/telus-sdet-project:latest"
        ALLURE_RESULTS = "${WORKSPACE}/allure-results"
        MAVEN_OPTS = "-Dmaven.repo.local=${WORKSPACE}/.m2/repository -Xmx512m -XX:+UseContainerSupport"
    }

    stages {
		stage('Checkout') {
			steps {
				checkout([
                    $class: 'GitSCM',
                    extensions: [[$class: 'CleanBeforeCheckout']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/tejveer1031/Telus-SDET-Project',
                        credentialsId: 'github-token'
                    ]],
                    branches: [[name: '*/main']]
                ])
            }
        }

        stage('Build & Test') {
			steps {
				script {
					sh """
                        mvn clean test \
                            -Dmaven.test.failure.ignore=true \
                            -Dallure.results.directory=${ALLURE_RESULTS}
                    """
                }
            }
            post {
				always {
					archiveArtifacts artifacts: '**/target/surefire-reports/**/*', allowEmptyArchive: true
                }
            }
        }

        stage('Docker Build') {
			steps {
				withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
					sh """
                        docker build \
                            --no-cache \
                            --memory=1g \
                            --cpus=0.8 \
                            -t ${DOCKER_IMAGE} .
                    """
                }
            }
        }

        stage('Allure Report') {
			steps {
				allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: "${ALLURE_RESULTS}"]],
                    reportBuildPolicy: 'ALWAYS',
                    commandline: 'allure'
                ])
            }
        }

        stage('Push Image') {
			when {
				branch 'main'
                beforeAgent true
            }
            steps {
				withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
					sh """
                        docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                        docker push ${DOCKER_IMAGE}
                    """
                }
            }
        }
    }

    post {
		always {
			script {
				sh 'docker logout || true'
                sh 'docker system prune -af --volumes || true'
                cleanWs()
            }
        }
        success {
			emailext(
                subject: "✅ SUCCESS: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """|Allure Report: ${env.BUILD_URL}allure/
                         |Build URL: ${env.BUILD_URL}""".stripMargin(),
                to: 'Tejihayer.92@gmail.com',
                attachLog: true
            )
        }
        failure {
			emailext(
                subject: "❌ FAILURE: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """|Allure Report: ${env.BUILD_URL}allure/
                         |Console Output: ${env.BUILD_URL}console
                         |Build URL: ${env.BUILD_URL}""".stripMargin(),
                to: 'Tejihayer.92@gmail.com',
                attachLog: true
            )
        }
    }
}