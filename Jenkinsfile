pipeline {
	agent {
		docker {
			image 'docker:dind'
            args '--privileged -v /var/run/docker.sock:/var/run/docker.sock -u 0:0' // Run as root to bypass permission issues
        }
    }

    tools {
		jdk 'jdk21'
        maven 'Maven3'
    }

    environment {
		DOCKER_IMAGE = "tejveer001/telus-sdet-project:latest"
        ALLURE_RESULTS = "${WORKSPACE}/allure-results"
        DOCKER_BUILDKIT = "1"  // Enable Docker BuildKit
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

        stage('Docker Build & Test') {
			steps {
				withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
					script {
						sh """
                            docker build \
                                --progress=plain \
                                --build-arg MAVEN_OPTS="-Dmaven.repo.local=${WORKSPACE}/.m2/repository" \
                                -t ${DOCKER_IMAGE} .

                            docker run --rm \
                                -v ${ALLURE_RESULTS}:/app/target/allure-results \
                                -v ${WORKSPACE}/.m2:/root/.m2 \
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
                sh 'docker system prune --volumes --force || true'
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