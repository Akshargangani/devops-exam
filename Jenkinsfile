pipeline {
    agent any

    tools {
        maven 'Maven-3.9.jenkins'   // must match Jenkins Global Tool name
        jdk 'jdk-17'                // must match exactly (case-sensitive)
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Akshargangani/devops-exam'
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