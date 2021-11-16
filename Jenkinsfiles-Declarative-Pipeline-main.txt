pipeline{
    agent any
    tools{
        maven 'maven3.8.3'
    }
    stages{
        stage('CheckoutCode'){
            steps{
                git branch: 'development', credentialsId: 'c5cc656c-f1d6-4d2e-b628-f8bb05e3afe4', url: 'https://github.com/Devops-kamlesh/maven-web-application.git'
            }
        }
            stage('Build'){
                steps{
                    sh "mvn clean package"
                }
            }
            stage('SonarQubeReport'){
                steps{
                    sh "mvn sonar:sonar"
                }
            }
            stage('UploadArtifactIntoNexus'){
                steps{
                    sh "mvn deploy"
                }
            }
            stage('DeployAppToTomcat'){
                steps{
                   deploy adapters: [tomcat9(credentialsId: 'ac389609-4d0d-49a1-b1dc-94abaa6acf9a', path: '', url: 'http://3.110.101.96:8955/')], contextPath: null, war: '*/maven-web-application.war'
                }
            }
    }//closing stages
}//closing pipeline