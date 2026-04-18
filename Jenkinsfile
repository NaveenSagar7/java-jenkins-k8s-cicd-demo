pipeline {

    agent any

    environment {
        DOCKERHUB_USER = "naveen352"
        DOCKER_IMAGE = "${DOCKERHUB_USER}/java-demo"
        VERSION = "v${BUILD_NUMBER}"

    }

    stages {
        /*
        stage('Checkout') {
            steps {
                git 'https://github.com/username/java-jenkins-k8s-cicd-demo.git'
            }
        }
            */
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
                waitForQualityGate abortPipeline: true
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

        /*
        stage('Optional : Run in Docker container in the jenkins agent') {
            steps {
                sh "docker run -d -p 8081:8081 $DOCKER_IMAGE:$VERSION"
            }
        }
        */
        
        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    sh '''
                    echo "===== DEPLOYING TO REAL K8s CLUSTER ====="

                    kubectl config get-contexts
                    kubectl get nodes

                    kubectl apply -f deployment.yaml

                    kubectl rollout status deployment/java-demo

                    if [$? -ne 0 ]; then
                        echo "Deployment failed, rolling back..."
                        kubectl rollout undo deployment/java-demo
                        exit 1
                    fi
                    '''
                }
            }
        }
            
    }
}
