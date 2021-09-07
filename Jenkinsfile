pipeline {
    agent any

    stages {
        stage('拉取代码') {
            steps {
               checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: '05bad6fc-2115-4e94-a96d-e7f020f2a7fa', url: 'git@github.com:CatchingFireStage/fruits.git']]])
            }
        }
         stage('停止运行java程序'){
              steps {
                 sh 'sudo docker-compose -f run-docker-compose.yml down'
              }
         }
        stage('停止maven容器内打包'){
            steps {
                 sh 'sudo docker-compose -f build-docker-compose.yml down'
            }
        }
        stage('运行maven容器内打包'){
             steps {
                 sh 'sudo docker-compose -f build-docker-compose.yml up'
             }
        }
        stage('开始运行java程序'){
              steps {
                 sh 'sudo docker-compose -f run-docker-compose.yml up -d'
              }
        }
        stage('是否上线'){
             steps {
                Boolean  isOnLine = onLine
                echo "是否上线${isOnLine}"
             }
        }
    }
}
