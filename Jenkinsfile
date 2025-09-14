pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = "mySpringBootRegistry.azurecr.io"
        DOCKER_IMAGE = "lms-azure-demo"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sadik789diwan/lms-azure-demo.git'
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
