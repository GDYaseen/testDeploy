pipeline {
    agent any
    triggers {
        githubPush()
    }
    stages {
        stage('checkout') {
            steps {
                withCredentials([string(credentialsId: 'github-pat', variable: 'GITHUB_PAT')]) {
                    script {
                        if (fileExists('back2end2')) {
                            dir('back2end2') {
                                sh "git reset --hard" 
                                sh "git clean -fd" 
                                sh "git pull origin main"
                            }
                        } else {
                            sh "git clone https://${GITHUB_PAT}@github.com/TravelAgentPFS/back2end2.git"
                        }
                    }
                }
            }
        }
        stage('build') {
            steps {
                dir('back2end2'){
                    sh "mvn clean compile package"
                }
            }
        }
        stage('test') {
            steps {
                dir('back2end2'){
                    sh "mvn test"
                }
            }
        }
        stage('merge for deploy') {
            steps {
                withCredentials([string(credentialsId: 'github-pat', variable: 'GITHUB_PAT')]) {
                dir('back2end2'){
                    sh "git config --global user.email \"amri.amjado@gmail.com\" "
                    sh "git config --global user.name \"amja-do\" "
                    sh "git checkout deploy"
                    
                    sh "git add ."
                    sh "git commit -m \"deploy jar file\""
                    sh "git push https://${GITHUB_PAT}@github.com/TravelAgentPFS/back2end2.git deploy"
                }
                    
                }
            }
        }
    }
}
