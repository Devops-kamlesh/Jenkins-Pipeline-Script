pipeline{
    agent{
        label 'node1'
    }
    stages{
        //stage used for creating dir in specific location
        stage('Create-dir'){
            steps{
                dir('/tmp/test'){
                    sh 'touch test{1..4}.txt'
                }
            }

        }
    }//end of stages
}//end of pipeline