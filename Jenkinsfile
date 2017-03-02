pipeline {
  agent any 
  
    // Installs but doesn't put it on the path
    //tools {
    //    gradle "gradle_2"
    //}

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
        withEnv(["GRADLE_HOME=${tool name: 'gradle_2', type: 'hudson.plugins.gradle.GradleInstallation'}"]) {
          withEnv(["PATH=${env.PATH}:${env.GRADLE_HOME}/bin"]) {

              // Checking the env
              echo "GRADLE_HOME=${env.GRADLE_HOME}"
              echo "PATH=${env.PATH}"               
              sh 'set'
              sh 'gradle --version'
              sh 'gradle clean build test -i'
          }
        }
      }
    }
  }
}
