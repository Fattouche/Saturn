pipeline {
    agent any
    
    
    environment {
        SATURN_DRIVER = 'asdasd'
        SATURN_URL    = 'http://142.104.90.101:8008'
    }
    

    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package'
				sh 'rm src/main/resources/config/application-dev.yml'
				sh 'cp src/main/resources/config/application-dev-jenkins.yml src/main/resources/config/application-dev.yml'
                sh 'mkdir -p dist'
                sh 'cp target/saturn-1.0.4.war dist'
                sh 'mkdir -p dist/config'
                sh 'mkdir -p dist/mediaResources'
                sh 'cp src/main/resources/config/application-dev-jenkins.yml dist/config/application-dev.yml'
                sh 'java -jar dist/saturn-1.0.4.war 2> errorOutput.log > output.log &'
                sh 'sleep 30'
                sh 'cat output.log'
                sh 'cat errorOutput.log'
				sh 'mvn -Dtest=TestVault test'
                
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
