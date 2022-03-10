pipeline {
    agent any
     
    stages {
        stage('Ok') {
            steps {
                echo "Ok"
            }
        }
    }
    post {
        always {
            mail bcc: '', body: ' Build_Execute_Successfully', cc: '', from: '', replyTo: '', subject: 'Build_Status', to: 'kamleshray21@gmail.com'
        }
    }
}