pipeline {

    agent any

    environment {

        DOCKER_IMAGE = "dockerhub-user/java-demo"
        VERSION = "v${BUILD_NUMBER}"

    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/username/java-jenkins-k8s-cicd-demo.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $DOCKER_IMAGE:$VERSION ."
            }
        }

        stage('Push Image') {
            steps {
                withDockerRegistry([credentialsId: 'dockerhub-cred', url: '']) {
                    sh "docker push $DOCKER_IMAGE:$VERSION"
                }
            }
        }

        stage('Update Manifest') {
            steps {
                sh "./scripts/update_image.sh $VERSION"
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh "kubectl apply -f deployment.yaml"
            }
        }
        stage('Verify Deployment') {
            steps {
                sh "kubectl rollout status deployment/java-demo"
            }
        }

        stage('Rollback if Failed') {
            steps {
                script {
                    def status = sh(script: "kubectl rollout status deployment/java-demo", returnStatus: true)
        
                    if (status != 0) {
                        sh "kubectl rollout undo deployment/java-demo"
                        error "Deployment failed. Rolled back."
                    }
                }
            }
        }

    }
}
