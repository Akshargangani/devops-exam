/**
 * Jenkinsfile for Spring Boot CRUD Application CI/CD Pipeline
 * 
 * This pipeline performs the following stages:
 * 1. Clone repository from GitHub
 * 2. Build the project using Maven
 * 3. Run unit tests
 * 4. Package the application (generate .jar)
 * 5. Archive artifacts
 * 6. Build Docker image (optional)
 * 
 * Prerequisites:
 * - Jenkins with Maven and JDK 17 configured
 * - Docker installed (for Docker stage)
 * - GitHub webhook configured (for automatic triggers)
 */

pipeline {
    agent any

    environment {
        // Application configuration
        APP_NAME = 'crud-application'
        VERSION = '1.0.0'
        
        // Maven configuration
        MAVEN_OPTS = '-Xmx1024m'
        
        // Docker configuration (optional)
        DOCKER_REGISTRY = 'your-docker-registry.com'  // Change to your registry
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/${APP_NAME}:${VERSION}"
        
        // Git configuration
        GIT_BRANCH = 'main'
        GIT_URL = 'https://github.com/your-username/crud-application.git'  // Change to your repo
    }

    tools {
        // Requires Maven and JDK to be configured in Jenkins Global Tool Configuration
        maven 'Maven-3.9.x'  // Update with your Maven installation name
        jdk 'JDK-17'         // Update with your JDK installation name
    }

    options {
        // Build options
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timestamps()
        ansiColor('xterm')
    }

    triggers {
        // Poll SCM every 5 minutes (alternative to webhook)
        // pollSCM('H/5 * * * *')
        
        // GitHub webhook trigger (requires GitHub Jenkins plugin)
        githubPush()
    }

    stages {
        
        // Stage 1: Clone Repository
        stage('Clone Repository') {
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Clone Repository'
                    echo '========================================'
                }
                
                // Clean workspace before cloning
                cleanWs()
                
                // Clone the repository
                git branch: "${GIT_BRANCH}",
                    url: "${GIT_URL}",
                    credentialsId: 'github-credentials'  // Update with your credentials ID
                
                // Display repository information
                sh 'git log -1 --oneline'
                sh 'git branch -a'
            }
        }

        // Stage 2: Build Project
        stage('Build Project') {
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Build Project'
                    echo '========================================'
                }
                
                // Run Maven clean and compile
                sh 'mvn clean compile'
                
                // Display build info
                sh 'mvn --version'
            }
        }

        // Stage 3: Run Unit Tests
        stage('Run Tests') {
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Run Unit Tests'
                    echo '========================================'
                }
                
                // Run Maven tests
                sh 'mvn test'
            }
            post {
                always {
                    // Publish test results
                    junit testResults: '**/target/surefire-reports/*.xml'
                    
                    // Publish JaCoCo coverage report
                    jacoco execPattern: '**/target/jacoco.exec',
                           classPattern: '**/target/classes',
                           sourcePattern: '**/src/main/java'
                }
            }
        }

        // Stage 4: Code Quality Analysis (Optional)
        stage('Code Quality') {
            when {
                expression {
                    // Run only on main branch or PR
                    return env.BRANCH_NAME == 'main' || env.CHANGE_ID != null
                }
            }
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Code Quality Analysis'
                    echo '========================================'
                }
                
                // Run Maven verify phase which includes checks
                sh 'mvn verify -DskipTests'
                
                // SonarQube analysis (optional - requires SonarQube plugin)
                // withSonarQubeEnv('SonarQube') {
                //     sh 'mvn sonar:sonar'
                // }
            }
        }

        // Stage 5: Package Application
        stage('Package Application') {
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Package Application'
                    echo '========================================'
                }
                
                // Skip tests as they already ran
                sh 'mvn package -DskipTests'
                
                // Verify JAR file was created
                sh 'ls -lh target/*.jar'
            }
        }

        // Stage 6: Archive Artifacts
        stage('Archive Artifacts') {
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Archive Artifacts'
                    echo '========================================'
                }
                
                // Archive the JAR file and other artifacts
                archiveArtifacts artifacts: 'target/*.jar',
                                fingerprint: true,
                                onlyIfSuccessful: true
                
                // Archive test reports
                archiveArtifacts artifacts: 'target/surefire-reports/**',
                                allowEmptyArchive: true
            }
        }

        // Stage 7: Build Docker Image (Optional)
        stage('Build Docker Image') {
            when {
                expression {
                    // Only build Docker image on main branch
                    return env.BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Build Docker Image'
                    echo '========================================'
                }
                
                // Build Docker image
                script {
                    docker.build("${DOCKER_IMAGE}")
                }
                
                // Optional: Push to Docker registry
                // script {
                //     docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-credentials') {
                //         docker.image("${DOCKER_IMAGE}").push()
                //     }
                // }
            }
        }

        // Stage 8: Deploy (Optional - based on environment)
        stage('Deploy') {
            when {
                expression {
                    // Only deploy on main branch
                    return env.BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    echo '========================================'
                    echo 'Stage: Deploy Application'
                    echo '========================================'
                }
                
                // Deployment steps can be added here
                // Examples:
                // - Copy JAR to server
                // - Restart service
                // - Deploy to Kubernetes
                // - Deploy to cloud platform
                
                echo 'Deployment stage - Add your deployment logic here'
            }
        }

    }

    post {
        // Actions to run after pipeline completion
        
        always {
            // Clean up workspace
            cleanWs(deleteDirs: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.git', type: 'EXCLUDE']])
        }
        
        success {
            // Actions on successful build
            echo '✅ Pipeline completed successfully!'
            
            // Send notification (optional)
            // slackSend(color: 'good', message: "Build Successful: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
            // emailext(subject: "Build Successful: ${env.JOB_NAME}",
            //          body: "Good news! The build was successful.",
            //          to: "${env.CHANGE_AUTHOR_EMAIL}")
        }
        
        failure {
            // Actions on failed build
            echo '❌ Pipeline failed!'
            
            // Send notification (optional)
            // slackSend(color: 'danger', message: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}")
            // emailext(subject: "Build Failed: ${env.JOB_NAME}",
            //          body: "The build has failed. Please check the logs.",
            //          to: "${env.CHANGE_AUTHOR_EMAIL}")
        }
        
        unstable {
            // Actions on unstable build
            echo '⚠️ Pipeline unstable!'
        }
        
        changed {
            // Actions when build status changes
            echo '📝 Build status has changed!'
        }
    }

}
