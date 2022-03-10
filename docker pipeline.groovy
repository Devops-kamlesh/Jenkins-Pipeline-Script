pipeline{
    agent any
    tools{
        maven "maven3.8.4"
    }
   options {
  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr: '', numToKeepStr: '5')
   }
    stages{
        stage('checkoutcode'){
            steps{
               git credentialsId: '19fdd14a-5a65-4c2a-8590-cd5d368f59c1', url: 'https://github.com/kamleshray55/maven-web-application.git'
            }
        }
        stage('build'){
        steps{
        sh "mvn clean package"
        }
        }
        stage('docker-image'){
            steps{
                sh "docker build -t docker8955/maven-web-application:${BUILD_NUMBER} ."
            }
        }
        stage('image push'){
            steps{
                sh "docker login -u docker8955 -p R44DVFFB244pJRv"
                sh "docker push docker8955/maven-web-application:${BUILD_NUMBER}"
            }
        }
        stage('remove-docker container'){
            steps{
                sshagent(['35.154.29.240']) {
               sh ''' ssh -o StrictHostKeyChecking=no ubuntu@172.31.37.200 "docker ps -aq | xargs docker rm -f | true" '''
                }
            }
        }
        stage('deploy docker image'){
            steps{
                sshagent(['35.154.29.240']) {
               sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.37.200 docker run -d -p 8955:8080 --name mavenwebapp docker8955/maven-web-application:${BUILD_NUMBER}"
        }
            }
        }
    }//end of stages
}//end of pipeline