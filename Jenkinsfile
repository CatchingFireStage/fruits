pipeline {
    agent any

    stages {
        stage('拉取代码') {
            steps {
               checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: '05bad6fc-2115-4e94-a96d-e7f020f2a7fa', url: 'git@github.com:CatchingFireStage/fruits.git']]])
            }
        }
        stage('是否上线'){
             steps {
                def _onLine = params.onLine
                echo "是否上线${_onLine}"
             }
        }
    }
}
