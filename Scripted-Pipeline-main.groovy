node{
    //config maven to run maven command 
    def mavenHome = tool name:"maven3.8.3"
        //1st build trigger 
    properties([[$class: 'JobLocalConfiguration', changeReasonComment: ''], pipelineTriggers([cron('* * * * *')])])
    //2nd delete old build
    properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '2', daysToKeepStr: '', numToKeepStr: '2')), [$class: 'JobLocalConfiguration', changeReasonComment: '']])
        //pull code form git
        stage('CheckoutCode'){
        git branch: 'test', credentialsId: 'c5cc656c-f1d6-4d2e-b628-f8bb05e3afe4', url: 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
    }
    //using maven make jar,war,and ear file 
    stage('Build'){
     sh "${mavenHome}/bin/mvn clean package"
    }
    //execute sonarQube report for artifacts
    stage('SonarQubeReport'){
     sh "${mavenHome}/bin/mvn sonar:sonar"
    }
    //upload artifacts to remote repo
    stage('UploadToRemoteRepo'){
        sh "${mavenHome}/bin/mvn deploy"
    }
    //deploy war or jar file to tomcat server
    stage('DeployApplicationToTomcat'){
deploy adapters: [tomcat9(credentialsId: 'ac389609-4d0d-49a1-b1dc-94abaa6acf9a', path: '', url: 'http://3.109.56.22:8955/')], contextPath: null, war: '*/maven-web-application.war'
    }
}//end of node 