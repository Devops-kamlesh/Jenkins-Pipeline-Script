pipeline{
    agent any
    tools{
        maven "maven3.8.4"
    }
    options {
  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr: '', numToKeepStr: '5')
    }
    stages{
        stage('checkout-code'){
            steps{
            git credentialsId: '19fdd14a-5a65-4c2a-8590-cd5d368f59c1', url: 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
            }
        }
        stage('build-artifacts'){
            steps{
                sh "mvn clean package"
            }
        }
        stage('build-docker-image'){
            steps{
                sh "docker build -t docker8955/maven-web-application:${BUILD_NUMBER} ."
            }
        }
        stage('docker-push'){
            steps{
                sh "docker login -u docker8955 -p R44DVFFB244pJRv"
                sh "docker push docker8955/maven-web-application:${BUILD_NUMBER}"
            }
        }
        stage('remove-running-container'){
            steps{
                sshagent(['35.154.29.240']) {
                    sh ''' ssh -o StrictHostKeyChecking=no ubuntu@172.31.37.200 "docker ps -aq | xargs docker rm -f | true" '''
                }

            }
        }
        stage('docker-run'){
            steps{
                sshagent(['35.154.29.240']) {
             sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.37.200 docker run -d -p 8080:8080 --name mavenwebapp docker8955/maven-web-application:${BUILD_NUMBER}"
          }
            }
        }
    }//end of stages
    post {
  always {
    cleanWs()
  }
success {
    mail bcc: '', body: ' Build_Execute_Successfully', cc: '', from: '', replyTo: '', subject: 'Build_Status', to: 'kamleshray21@gmail.com'
  }
    }
}//end of pipeline