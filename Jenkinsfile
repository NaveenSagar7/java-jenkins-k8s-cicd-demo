pipeline {

    agent any

    environment {
        DOCKERHUB_USER = "naveen352"
        DOCKER_IMAGE = "${DOCKERHUB_USER}/java-demo"
        VERSION = "v${BUILD_NUMBER}"
        USER = "ubuntu"
        VM_IP = "172.31.45.39"

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


        stage('Run in Docker container in the jenkins agent') {
            steps {
                sh "docker rm -f java-demo || true" // Remove existing container if it exists
                sh "docker run -d --name java-demo -p 8081:8081 $DOCKER_IMAGE:$VERSION" 
            }
        }
        
        stage('Deploy to Kubernetes on a minikube cluster in a different vm') {
            steps {
                sshagent(['kubernetes-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ${USER}@${VM_IP} << 'EOF'

                    kubectl apply -f deployment.yaml

                    kubectl rollout status deployment/java-demo
                    STATUS=$?

                    if [ $STATUS -ne 0 ]; then
                        echo "Deployment failed. Rolling back..."
                        kubectl rollout undo deployment/java-demo
                        exit 1
                    fi

                    EOF
                    '''
                }
                
            }
        }
    }
}
