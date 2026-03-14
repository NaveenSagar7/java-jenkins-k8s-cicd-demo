# Step-by-Step Pipeline Explanation

## 1. Developer Push

The CI/CD process begins when a developer pushes code to the GitHub
repository. This repository contains the application code, Kubernetes
manifests, Dockerfile, and Jenkinsfile.

## 2. GitHub Webhook

A GitHub webhook automatically triggers the Jenkins pipeline whenever a
new commit is pushed. This enables continuous integration by ensuring
every change is automatically tested and deployed.

## 3. Jenkins Pipeline Execution

Jenkins reads the Jenkinsfile from the repository and starts executing
the defined pipeline stages sequentially.

------------------------------------------------------------------------

## Checkout Stage

Jenkins clones the source code from the GitHub repository into the
Jenkins workspace.

    git https://github.com/username/java-jenkins-k8s-cicd-demo.git

This ensures Jenkins always builds the latest version of the
application.

------------------------------------------------------------------------

## Maven Build Stage

The application is compiled and packaged using Maven.

    mvn clean package

This step generates the application artifact (JAR/WAR file) in the
`target/` directory.

------------------------------------------------------------------------

## SonarQube Analysis Stage

Jenkins runs static code analysis using SonarQube.

    mvn sonar:sonar

SonarQube scans the code for:

-   Bugs
-   Code smells
-   Security vulnerabilities
-   Code coverage
-   Technical debt

The results are stored in the SonarQube dashboard.

------------------------------------------------------------------------

## Quality Gate Stage

Jenkins waits for SonarQube to evaluate the analysis results against
predefined quality gate rules.

Example rules may include:

-   Minimum code coverage
-   No critical vulnerabilities
-   Acceptable duplication level

If the quality gate fails, the pipeline is automatically aborted.

    waitForQualityGate abortPipeline: true

This prevents low-quality code from reaching production.

------------------------------------------------------------------------

## Docker Build Stage

If the quality gate passes, Jenkins builds a Docker image for the
application.

    docker build -t dockerhub-user/java-demo:v${BUILD_NUMBER} .

Each build is tagged using the Jenkins build number to maintain
versioned images.

------------------------------------------------------------------------

## Docker Push Stage

The Docker image is pushed to DockerHub using Jenkins credentials.

    docker push dockerhub-user/java-demo:v${BUILD_NUMBER}

This allows Kubernetes to pull the latest application image.

------------------------------------------------------------------------

## Update Kubernetes Manifest

A script updates the Kubernetes deployment manifest with the new Docker
image version.

    ./scripts/update_image.sh v${BUILD_NUMBER}

This ensures the Kubernetes deployment always references the latest
image.

------------------------------------------------------------------------

## Deploy to Kubernetes

Jenkins deploys the updated application to the Kubernetes cluster using
kubectl.

    kubectl apply -f deployment.yaml

Kubernetes then schedules new pods with the updated container image.

------------------------------------------------------------------------

## Deployment Verification

Jenkins verifies that the deployment rollout was successful.

    kubectl rollout status deployment/java-demo

This command waits until all pods are running successfully.

------------------------------------------------------------------------

## Automatic Rollback (Failure Handling)

If the deployment fails or pods do not start correctly, Jenkins
automatically rolls back to the previous stable version.

    kubectl rollout undo deployment/java-demo

This ensures production stability and minimizes downtime.
