pipeline{
    agent any
    stages{
        stage('checkoutcode'){
            steps{
                git credentialsId: 'c5cc656c-f1d6-4d2e-b628-f8bb05e3afe4', url: 'https://github.com/Devops-kamlesh/nodejs-app-mss.git'
            }
        }
        stage('build'){
            steps{
                nodejs(nodeJSInstallationName: 'nodejs17.1.0'){
                  sh 'npm install'  
                } 
            }
        }
        stage('ExecuteSonarQubeReport'){
            steps{
                nodejs(nodeJSInstallationName: 'nodejs17.1.0'){
                  sh 'npm run sonar'  
                } 
            }
        }
        stage('Upload-nexus'){
            steps{
                nodejs(nodeJSInstallationName: 'nodejs17.1.0'){
                  sh 'npm publish'  
                } 
            }
        }
    }//end of stages
}//end of pipeline 