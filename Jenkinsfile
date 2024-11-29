pipeline {
    agent any
    
    environment {
        AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_KEY')
        AWS_REGION = 'ca-central-1'
        SONARQUBE_URL = 'http://15.223.67.216:9000'
        SONARQUBE_TOKEN = credentials('SONAR_TOKEN')
        NEXUS_URL = 'http://3.96.217.72:8082'  // Replace with your Nexus URL
        NEXUS_REPO = 'maven-nexus-repo'            // Replace with your Nexus repository ID
        NEXUS_USERNAME = credentials('NEXUS-USER') // Nexus username credentials ID
        NEXUS_PASSWORD = credentials('NEXUS-PASSWORD') 
    }

    stages {
        stage("Git Clone"){
            steps{
                git branch: 'main', url: 'https://github.com/NesnuKurian/scale-spring-app.git'
            }
        }
        // Stage 1: Pull Docker Image from Docker Hub
        stage('Pull Docker Image') {
            steps {
                script {
                    def appImage = docker.image('nesnukurian/scale-spring-app:latest')
                    appImage.pull()
                }
            }
        }

        // Stage 2: Install AWS CLI
        stage('Install AWS CLI') {
            steps {
                script {
                    sh '''
                    # Download AWS CLI
                    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"

                    # Unzip the file, force overwrite any existing files
                    unzip -o awscliv2.zip

                    # Install AWS CLI with sudo (ensure Jenkins has sudo privileges)
                    sudo ./aws/install --update
                    '''
                }
            }
        }

        // Stage 3: Launch EC2 Instance
        stage('Launch EC2 Instance') {
            steps {
                script {
                    sh '''
                    # Launch EC2 instance using AWS CLI
                    INSTANCE_ID=$(aws ec2 run-instances \
                        --image-id ami-0eb9fdcf0d07bd5ef \
                        --instance-type t2.micro \
                        --key-name new-key-pair \
                        --count 1 \
                        --user-data file:///home/ubuntu/install.sh \
                        --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=MyJenkinsEC2}]' \
                        --query 'Instances[0].InstanceId' \
                        --output text)
                    
                    echo "$INSTANCE_ID" > instance_id.txt
                    
                    echo "EC2 Instance ID: $INSTANCE_ID"
                    
                    # Wait for the instance to be running
                    aws ec2 wait instance-running --instance-ids $INSTANCE_ID
                    
                    echo "EC2 instance is now running!"
                    '''
                }
            }
        }

        // Stage 4: Wait for EC2 to Initialize
        stage('Wait for EC2 to Initialize') {
            steps {
                script {
                    echo "Waiting for EC2 instance to fully initialize..."
                    sleep(time: 2, unit: 'MINUTES')  // Adjust the delay as needed
                }
            }
        }

        // Stage 5: Deploy Docker Image to EC2
        stage('Deploy Docker Image to EC2') {
            steps {
                script {
                    // Load the INSTANCE_ID from the previous stage
                    def instanceId = readFile('instance_id.txt').trim()

                    withCredentials([sshUserPrivateKey(credentialsId: 'new-ec2-ssh-key', keyFileVariable: 'SSH_KEY')]) {
                        sh '''
                        # Get the public IP of the EC2 instance
                        PUBLIC_IP=$(aws ec2 describe-instances \
                            --instance-ids ''' + instanceId + ''' \
                            --query 'Reservations[0].Instances[0].PublicIpAddress' \
                            --output text)
                        
                        echo "EC2 Public IP: $PUBLIC_IP"
                        
                        # SSH into the EC2 instance and deploy the Docker image
                        ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i $SSH_KEY ubuntu@$PUBLIC_IP \
                        "sudo docker pull nesnukurian/scale-spring-app:latest && \
                         sudo docker run -d -p 8081:8081 nesnukurian/scale-spring-app:latest"
                        '''
                    }
                }
            }
        }
        // **New SonarQube Stage**
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {  // 'SonarQube' is the name of your SonarQube server in Jenkins
                        // If this is a Maven project:
                        sh 'mvn sonar:sonar -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.login=${SONARQUBE_TOKEN}'
                        
                                                    }
                        }
                 }
            }
            
        // Stage 7: Publish to Nexus
        stage('Publish to Nexus') {
            steps {
                script {
                    withMaven(globalMavenSettingsConfig: 'global-settings', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                        try {
                            dir("${env.WORKSPACE}") {  // Ensure correct directory context
                                sh '''
                                mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_URL}/repository/${NEXUS_REPO} \
                                           -Dusername=${NEXUS_USERNAME} -Dpassword=${NEXUS_PASSWORD}
                                '''
                            }
                        } catch (Exception e) {
                            error "Failed to publish to Nexus: ${e.message}"
                        }
                    }
                }
            }
        }
        


    }
}

