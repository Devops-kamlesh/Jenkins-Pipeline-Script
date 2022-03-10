pipeline{
    agent any
    tools{
        maven 'maven3.8.4'
    }
    stages{
        stage('checkout-code'){
            steps{
                git 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
            }
        }
        stage('build'){
            steps{
                sh "mvn clean package"
            }
        }
        stage('docker-image'){
            steps{
                sh "docker build -t 219594658719.dkr.ecr.ap-south-1.amazonaws.com/maven-web-app:$BUILD_NUMBER ."
            }
        }
        stage('docker-login'){
            steps{
                sh "aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 219594658719.dkr.ecr.ap-south-1.amazonaws.com"
                sh "docker push 219594658719.dkr.ecr.ap-south-1.amazonaws.com/maven-web-app:$BUILD_NUMBER"
            }
        }
        stage('docker-compose'){
            steps{
             sh " sed -i 's/latest/$BUILD_NUMBER/g' docker-compose.yml"
             sh "scp -i ~/RedHat.pem -o StrictHostKeyChecking=no docker-compose.yml ubuntu@192.168.0.179:/tmp/docker-compose.yml"
            }
        }
        stage('deploy docker-image'){
            steps{
                sshagent(['0deec9e9-4aae-4699-8bc7-789d9fe1381e']) {
             sh "aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 219594658719.dkr.ecr.ap-south-1.amazonaws.com"
             sh '''ssh -o StrictHostKeyChecking=no ubuntu@192.168.0.179 "docker-compose -f /tmp/docker-compose.yml up -d" '''
}
            }
        }
    }//end of stages
}//end of pipeline