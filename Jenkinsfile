pipeline {
  agent any 
  
    tools {
        gradle "gradle-latest"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    triggers {
        pollSCM('*/5 * * * *')
    }
  
  stages {
    
    stage('Checkout') {
        steps {
            deleteDir()
            checkout scm
        }
    }
       
    stage('build') {
      steps {
        sh 'gradle --version'
        sh 'gradle clean build test -i'
      }
    }
  }
}
