pipeline{
    agent{
        label "node1"
    }
    //adding tools to jenkins
    tools{
        maven "maven3.8.3"
    }
    parameters{
    choice(choices: ['master', 'development'], name: 'Branch_name')
    }
    options{
        //discard old build
     buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr: '', numToKeepStr: '5')
    //adding timestamp to console-output
    timestamps()
    //time out if build stuck
    timeout(time: 60, unit: 'SECONDS')
        }//end of option 
        //build trigger
           /* triggers {
  cron '* * * * *'
}*/
        stages{
            //git pull 
        stage('checkoutcode'){
            steps{
                git branch: 'development', credentialsId: 'c5cc656c-f1d6-4d2e-b628-f8bb05e3afe4', url: 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
            }
        }
        stage(build){
            steps{
                sh "mvn clean package"
            }
        }
        stage("excute-sonar-qube-report"){
            steps{
                sh "mvn sonar:sonar"
            }
        }
    }//end of stages
    post('sending email notification'){
        success{
         mail bcc: '', body: 'build success', cc: '', from: '', replyTo: '', subject: 'build status', to: 'kamleshray21@gmail.com'
        }
        failure{
          mail bcc: '', body: 'build fail', cc: '', from: '', replyTo: '', subject: 'build status', to: 'kamleshray21@gmail.com'
        }
        aborted{
          mail bcc: '', body: 'build abort', cc: '', from: '', replyTo: '', subject: 'build status', to: 'kamleshray21@gmail.com'
        }
        always{
            cleanWs()
        }
   }//end of post
}//end of pipeline