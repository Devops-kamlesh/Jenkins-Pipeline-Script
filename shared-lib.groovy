
def call(stageName){

    if ( "${stageName}" == "build")
    {
        sh 'mvn clean package'
    }
    else if ( "${stageName}" == "SonarQube Report")
    {
        sh 'mvn sonar:sonar'
    }
    else if ( "${stageName}" == "Upload Into Nexus")
    {
        sh 'mvn deploy'
    }
}



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
            stage('build'){
                steps{
                    jenkins('build')
                }
            }
    }//end of stages
}//end of pipeline