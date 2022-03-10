pipeline{
    agent {
  label 'node1'
}
parameters{
        choice(choices: ['master', 'development', 'test'], description: 'build by parameter.', name: 'branchName')
        string(defaultValue: 'kamleshRay', name: 'personName')
    }
    //adding time-stamps,delete old-build
    options{
        timestamps()
        // delete old build
                buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '3', daysToKeepStr: '', numToKeepStr: '3'))
        //time out if build stuck
        timeout(time: 60, unit: 'SECONDS')
        }
    //trigger the job
    /*triggers {
       cron('H/2 * * * *')
    }*/
    tools{
        maven 'maven3.8.3'
    }
             stages{
        stage('Checkout-code'){
            steps{
                git branch: 'test', credentialsId: 'c5cc656c-f1d6-4d2e-b628-f8bb05e3afe4', url: 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
            }
        }
        stage('Build'){
            steps{
                sh 'mvn clean package'
            }
        }
        stage('SonarQubeReport'){
            steps{
                sh 'mvn sonar:sonar'
            }
        }
        stage('UploadArtifacts'){
            steps{
                sh 'mvn deploy'
            }
        }
        stage('Deploy-App-Tomcat'){
            steps{
                deploy adapters: [tomcat9(credentialsId: 'ac389609-4d0d-49a1-b1dc-94abaa6acf9a', path: '', url: 'http://3.110.92.170:8955/')], contextPath: null, war: '*/maven-web-application.war'
            }
        }
    }//closing stages
    post{
        success{
mail bcc: '', body: 'Build over successfully..', cc: 'kamleshray26@gmail.com', from: '', replyTo: '', subject: 'Build status', to: 'kamleshray21@gmail.com'
        }
        failure{
mail bcc: '', body: 'Build fail...', cc: 'kamleshray26@gmail.com', from: '', replyTo: '', subject: 'Build status', to: 'kamleshray21@gmail.com'
        }
        aborted{
mail bcc: '', body: 'Build abort...', cc: 'kamleshray26@gmail.com', from: '', replyTo: '', subject: 'Build status', to: 'kamleshray21@gmail.com'
        }
        always {
        cleanWs deleteDirs: true
    }
    }
    
 }//closing pipeline