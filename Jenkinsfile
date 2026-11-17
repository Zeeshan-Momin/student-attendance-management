
pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven 3'
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

        stage('Verify Build Environment') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
                bat '"C:\\Program Files\\nodejs\\node.exe" --version'
                bat '"C:\\Program Files\\nodejs\\npm.cmd" --version'
            }
        }

        stage('Backend Build and Tests') {
            steps {
                dir('backend') {
                    bat 'mvn -B clean verify'
                }
            }
            post {
                always {
                    junit testResults: 'backend/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Frontend Install and Build') {
            steps {
                dir('frontend') {
                    bat '"C:\\Program Files\\nodejs\\npm.cmd" ci'
                    bat '"C:\\Program Files\\nodejs\\npm.cmd" run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar, frontend/dist/**',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'Student Attendance Management CI pipeline completed successfully.'
        }
        failure {
            echo 'CI pipeline failed. Check the stage logs.'
        }
    }
}
