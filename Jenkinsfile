pipeline {
    agent any

    tools {
        maven 'Maven-3.9.jenkins'
        jdk 'jdk-17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Akshargangani/devops-exam'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
    }
}