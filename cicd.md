🔹 1. Typical Industry Pipeline (High-Level Flow)

Developer pushes code → GitHub/GitLab/Azure Repos.

Jenkins pipeline triggers:

Checkout code

Build Spring Boot app (mvn clean package / gradle build)

Run tests, code quality check with SonarQube

Build Docker image

Push Docker image to container registry (Azure Container Registry / DockerHub)

Deploy container to Azure service (AKS, App Service, or VM)

🔹 2. Azure Setup (from scratch)

You have an Azure account. You need to decide where you want to deploy the Spring Boot app:

✅ Common options in industry:

Azure App Service (PaaS, simple, good for Spring Boot microservices)

Azure Kubernetes Service (AKS) (containerized microservices, scalable, real-world enterprise choice)

Azure VM (less common nowadays, manual management overhead)

👉 For microservices with Docker, Azure Kubernetes Service (AKS) or Azure App Service for Containers is standard.

Steps to set up environment:

Create Azure Resource Group

az group create --name my-springboot-rg --location eastus


Create Azure Container Registry (ACR)

az acr create --resource-group my-springboot-rg --name mySpringBootRegistry --sku Basic


Create AKS Cluster (if using Kubernetes)

az aks create --resource-group my-springboot-rg --name mySpringBootCluster --node-count 2 --enable-addons monitoring --generate-ssh-keys


Connect AKS to ACR

az aks update -n mySpringBootCluster -g my-springboot-rg --attach-acr mySpringBootRegistry

🔹 3. Required Files in Your Spring Boot Repo
1. Dockerfile
   FROM openjdk:17-jdk-slim
   VOLUME /tmp
   COPY target/myapp-0.0.1-SNAPSHOT.jar app.jar
   ENTRYPOINT ["java","-jar","/app.jar"]

2. Jenkinsfile

Industries use a declarative pipeline in Jenkins. Example:

pipeline {
agent any

    environment {
        DOCKER_REGISTRY = "mySpringBootRegistry.azurecr.io"
        DOCKER_IMAGE = "springboot-app"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/springboot-app.git'
            }
        }

        stage('Build & Unit Test') {
            steps {
                sh 'mvn clean package -DskipTests=false'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t $DOCKER_REGISTRY/$DOCKER_IMAGE:$BUILD_NUMBER ."
            }
        }

        stage('Push to ACR') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'azure-acr-creds', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    sh "echo $PASSWORD | docker login $DOCKER_REGISTRY -u $USERNAME --password-stdin"
                    sh "docker push $DOCKER_REGISTRY/$DOCKER_IMAGE:$BUILD_NUMBER"
                }
            }
        }

        stage('Deploy to AKS') {
            steps {
                sh "kubectl set image deployment/springboot-deployment springboot-container=$DOCKER_REGISTRY/$DOCKER_IMAGE:$BUILD_NUMBER --record"
            }
        }
    }
}

3. Kubernetes Deployment File (deployment.yaml)
   apiVersion: apps/v1
   kind: Deployment
   metadata:
   name: springboot-deployment
   spec:
   replicas: 2
   selector:
   matchLabels:
   app: springboot-app
   template:
   metadata:
   labels:
   app: springboot-app
   spec:
   containers:
    - name: springboot-container
      image: mySpringBootRegistry.azurecr.io/springboot-app:latest
      ports:
        - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
name: springboot-service
spec:
type: LoadBalancer
selector:
app: springboot-app
ports:
- protocol: TCP
port: 80
targetPort: 8080

🔹 4. Jenkins Setup

Install Jenkins (VM or Docker).

Install plugins:

Git

Maven

Docker

SonarQube Scanner

Kubernetes CLI plugin

Configure SonarQube server in Jenkins.

Create credentials for ACR (azure-acr-creds).

Connect Jenkins to your GitHub/GitLab repo via webhook.

🔹 5. SonarQube Setup

Run SonarQube via Docker:

docker run -d --name sonar -p 9000:9000 sonarqube:latest


Configure in Jenkins under Manage Jenkins → Configure System → SonarQube.

🔹 6. Deployment to Azure

Jenkins will build, scan, dockerize, push to ACR, then run kubectl to update AKS deployment.

Kubernetes ensures zero-downtime rolling deployment by replacing pods gradually.

✅ Industry Best Practices

Use Helm charts instead of raw YAML for AKS deployments.

Store secrets in Azure Key Vault instead of config files.

Enable blue/green or canary deployment in Kubernetes for safer rollouts.

Use Azure DevOps Pipelines instead of Jenkins in some companies, but Jenkins is still widely used.