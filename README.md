# java-jenkins-k8s-cicd-demo

This repository demonstrates a traditional shell-based CI/CD deployment
pipeline using industry-standard DevOps tools.

## Pipeline Overview

GitHub → Jenkins → Maven → SonarQube → Docker → Kubernetes

### Pipeline stages

1.  Code pushed to GitHub
2.  GitHub webhook triggers Jenkins
3.  Jenkins builds code using Maven
4.  SonarQube performs static code analysis
5.  Quality gate validation
6.  Docker image build
7.  Docker image push to DockerHub
8.  Kubernetes deployment update
9.  kubectl deploys application

------------------------------------------------------------------------

## Prerequisites

Install the following tools:

-   Jenkins
-   Java 17
-   Maven
-   Docker
-   kubectl
-   Kubernetes cluster (Minikube or EKS)
-   SonarQube
-   DockerHub account

------------------------------------------------------------------------

## Jenkins Configuration

Install Jenkins plugins:

-   Git
-   Pipeline
-   Docker Pipeline
-   SonarQube Scanner
-   Kubernetes CLI

------------------------------------------------------------------------

## SonarQube Setup

Generate token:

SonarQube\
→ My Account\
→ Security\
→ Generate Token

Add in Jenkins:

Manage Jenkins\
→ Configure System\
→ SonarQube Servers

------------------------------------------------------------------------

## Docker Configuration

Add DockerHub credentials:

Manage Jenkins\
→ Credentials\
→ Username + Password

------------------------------------------------------------------------

## Kubernetes Setup

Configure kubeconfig on Jenkins server:

kubectl config view

Test connection:

kubectl get nodes

------------------------------------------------------------------------

## Running the Pipeline

Push code to GitHub.

git push origin main

GitHub webhook triggers Jenkins pipeline.

------------------------------------------------------------------------

## Expected Output

-   Build Success
-   SonarQube Quality Gate Passed
-   Docker Image Built
-   Docker Image Pushed
-   Kubernetes Deployment Updated
-   Pods Running

------------------------------------------------------------------------

## Verify Deployment

kubectl get pods\
kubectl get deployments

------------------------------------------------------------------------

## Troubleshooting

### SonarQube connection issue

Check:

-   SonarQube URL
-   Token
-   Plugin installation

### Docker push failure

Check:

-   DockerHub credentials
-   docker login

### Kubernetes deploy failure

Check:

-   kubectl config
-   cluster access
