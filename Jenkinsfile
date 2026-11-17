pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'JDK 17'
    }

    environment {
        CI = 'true'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend compile and test') {
            steps {
                dir('backend') {
                    sh 'mvn clean verify'
                }
            }
        }

        stage('Frontend install and build') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive artifacts') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar, frontend/dist/**', fingerprint: true
            }
        }
    }
}
