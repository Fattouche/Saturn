pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package'
				sh 'rm src/main/resources/config/application-dev.yml'
				sh 'cp src/main/resources/config/application-dev-jenkins.yml src/main/resources/config/application-dev.yml'
				sh 'cat src/main/resources/config/application-dev.yml'
				sh 'SATURN_DRIVER=firefox'
				sh 'SATURN_URL=http://localhost:8080'
				sh 'mvn -Dtest=TestSelenium'
            }
        }
    }
    post {
        failure {
            emailext (
                    subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                    body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                        <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider']],
                    to: 'fattouch@uvic.ca',
                    mimeType: 'text/html'
                )
        }
    }
}
