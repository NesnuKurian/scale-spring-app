pipeline {
    agent any

    environment {
        AWS_REGION = ca-central-1'
        ECR_REPOSITORY = 'scale-spring-app'
        GITHUB_REPO = 'https://github.com/Surinder07/scale-spring-app.git'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: "${GITHUB_REPO}"
            }
        }
}
}
