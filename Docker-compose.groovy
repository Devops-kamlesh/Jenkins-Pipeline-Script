pipeline{
    agent any
    tools{
maven 'maven3.8.4'
    }
    options {
  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr: '', numToKeepStr: '5')
   }
    stages{
    stage('checkout-code'){
steps{
git credentialsId: 'b7993551-2669-4ed1-9c74-5d907d106785', url: 'https://github.com/Devops-kamlesh/-spring-boot-mongo-docker.git'
}    
    }
    stage('build artifact'){
        steps{
            sh "mvn clean package"
        }
    }
    stage('build-docker_image'){
        steps{
            sh "docker build -t docker8955/spring-boot-mongo:$BUILD_NUMBER ."
        }
    }
    stage('upload-to-repository'){
        steps{
            sh "docker login -u docker8955 -p R44DVFFB244pJRv"
            sh "docker push docker8955/spring-boot-mongo:$BUILD_NUMBER"
            sh "docker push docker8955/spring-boot-mongo:$BUILD_NUMBER"
            sh "sed -i 's/VERSION/$BUILD_NUMBER/g' docker-compose.yml"
        }
    }
    stage('deploy-as-container'){
        steps{
            sshagent(['6d20cf51-58d4-449e-b65a-2e2635ac96f0']) {
    sh "scp -o StrictHostKeyChecking=no docker-compose.yml ec2-user@172.31.15.133:/tmp/dockerfile"
    sh '''ssh -o StrictHostKeyChecking=no ec2-user@172.31.15.133 "docker-compose -f /tmp/dockerfile/docker-compose.yml up -d" '''
}
        }
    }
    }//end of stages
}//end of pipeline