pipeline {
    agent any
    parameters{
        choice(choices: ['master', 'development', 'test'], description: 'build by parameter.', name: 'branchName')
        string(defaultValue: 'kamleshRay', name: 'personName')
    }
    stages {
        stage('CheckoutCode'){
            steps{
        git branch: 'test', credentialsId: 'c5cc656c-f1d6-4d2e-b628-f8bb05e3afe4', url: 'https://github.com/Devops-kamlesh/Maven-web-application-jenkins.git'
       sh "echo 'person-name is ${params.personName}'"  
    }
        }
    }//stages end
}//pipeline end
