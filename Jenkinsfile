pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package'
				sh 'rm src/main/resources/config/application-dev.yml'
				sh 'cp src/main/resources/config/application-dev-jenkins.yml src/main/resources/config/application-dev.yml'
				sh 'SATURN_DRIVER=firefox'
				sh 'SATURN_URL=http://localhost:8080'
				sh 'mvn -Dtest=TestSelenium'
            }
        }
    }
}
