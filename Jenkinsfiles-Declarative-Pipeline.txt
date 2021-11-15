pipeline{
    agent any 
    stages{
        stage('Stage1'){
            steps{
                echo "hello world"
            }
        }
        stage('step2'){
            steps{
                sh "cal"
            }
        }
        stage('step3'){
            steps{
                sh "date"
            }
        }
    }//closing stages
}//closing pipeline