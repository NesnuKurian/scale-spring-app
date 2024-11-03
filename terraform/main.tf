terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
  required_version = ">= 0.12"
}

provider "aws" {
  region = "ca-central-1"  # Change this to your preferred region
}

# Define a security group for the Spring Boot app
resource "aws_security_group" "spring_app_sg" {
  name        = "spring_app_sg"
  description = "Allow inbound traffic for Spring Boot app"

  ingress {
    from_port   = 8081  # Change this to your application's port (8081 based on your Dockerfile)
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allow traffic from anywhere (be careful with this in production)
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]  # Allow all outbound traffic
  }
}

# Create the EC2 instance
resource "aws_instance" "spring_app" {
  ami           = "ami-0eb9fdcf0d07bd5ef"  # Change this to a valid AMI ID for your region
  instance_type = "t2.micro"  # Choose your instance type
  key_name      = "jenkins-coop"  # Replace with your key pair name

  # Reference the security group created above
  security_groups = [aws_security_group.spring_app_sg.name]

  user_data = <<-EOF
              #!/bin/bash
              # Install Docker
              yum update -y
              amazon-linux-extras install docker -y
              service docker start
              usermod -a -G docker ec2-user
              # Pull the Docker image from Docker Hub
              docker pull nesnukurian/scale-spring-app:latest
              # Run the Docker container
              docker run -d -p 8081:8081 nesnukurian/scale-spring-app:latest
              EOF

  tags = {
    Name = "SpringBootApp"
  }
}
