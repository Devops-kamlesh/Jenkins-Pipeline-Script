node{
    stage('step1'){
        echo "hello world"
    }
    stage('step2'){
        sh "cal"
    }
    stage('stage3'){
        sh "date"
    }
}//closing node