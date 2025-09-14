pipeline {
    agent any

    environment {
        ACR_NAME = "springbootacr12345"
        IMAGE_NAME = "springboot-app"
        IMAGE_TAG = "v${env.BUILD_NUMBER}"
        RESOURCE_GROUP = "springboot-rg"
        AKS_CLUSTER = "springboot-aks"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/sadik789diwan/lms-azure-demo.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh """
                  docker build -t $ACR_NAME.azurecr.io/$IMAGE_NAME:$IMAGE_TAG .
                  az acr login --name $ACR_NAME
                  docker push $ACR_NAME.azurecr.io/$IMAGE_NAME:$IMAGE_TAG
                """
            }
        }

        stage('Deploy to AKS') {
            steps {
                sh """
                  az aks get-credentials --resource-group $RESOURCE_GROUP --name $AKS_CLUSTER --overwrite-existing
                  kubectl set image deployment/$IMAGE_NAME $IMAGE_NAME=$ACR_NAME.azurecr.io/$IMAGE_NAME:$IMAGE_TAG --record
                """
            }
        }
    }
}
