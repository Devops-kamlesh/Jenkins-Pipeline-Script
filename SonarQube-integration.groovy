node{
    def mavenHome = tool name: "maven3.8.4"
    stage('CheckoutCode'){
        git branch: 'development', credentialsId: '984d435b-2abc-4527-8b9b-82293f145ff4', url: 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
    }
    stage('Build'){
        sh "${mavenHome}/bin/mvn clean package"
    }
    stage('ExecuteSonarQubeReport'){
        withSonarQubeEnv('SonarQube'){ 
        sh "${mavenHome}/bin/mvn sonar:sonar"
    }
    } 
    stage('Quality Gate'){
          timeout(time: 2, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
          }
}
}
