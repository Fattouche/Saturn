pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
				sh 'SATURN_DRIVER=firefox'
				sh 'SATURN_URL=http://localhost:8080'
				sh 'mvn -Dtest=TestSelenium'
            }
        }
    }
}
