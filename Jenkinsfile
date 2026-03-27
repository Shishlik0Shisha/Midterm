pipeline {
  agent any

  environment {
    DOCKERHUB_REPO = 'shishlik0shisha/java-app'
    IMAGE_TAG = "${BUILD_NUMBER}"
    IMAGE_FULL = "${DOCKERHUB_REPO}:${IMAGE_TAG}"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build - JDK 17') {
      steps {
        sh '''
          docker exec java17-builder bash -lc "
            cd /var/jenkins_home/workspace/${JOB_NAME} &&
            mvn -B -Dmaven.test.skip=true clean package
          "
        '''
      }
    }

    stage('Unit Test - JDK 11') {
      steps {
        sh '''
          docker exec java11-tester bash -lc "
            cd /var/jenkins_home/workspace/${JOB_NAME} &&
            mvn -B test-compile surefire:test
          "
        '''
      }
    }

    stage('SonarQube Analysis - JDK 8') {
      steps {
        withSonarQubeEnv('sonarqube') {
          sh '''
            docker exec \
              -e SONAR_HOST_URL="$SONAR_HOST_URL" \
              -e SONAR_AUTH_TOKEN="$SONAR_AUTH_TOKEN" \
              java8-analyzer bash -lc "
                cd /var/jenkins_home/workspace/${JOB_NAME} &&
                mvn -B sonar:sonar \
                  -Dsonar.projectKey=mathutils \
                  -Dsonar.projectName=mathutils \
                  -Dsonar.host.url=$SONAR_HOST_URL \
                  -Dsonar.token=$SONAR_AUTH_TOKEN
              "
          '''
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        sh '''
          docker build -t "$IMAGE_FULL" -t "$DOCKERHUB_REPO:latest" .
        '''
      }
    }

    stage('Push Docker Image') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'dockerhub-creds',
          usernameVariable: 'DOCKER_USER',
          passwordVariable: 'DOCKER_PASS'
        )]) {
          sh '''
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker push "$IMAGE_FULL"
            docker push "$DOCKERHUB_REPO:latest"
          '''
        }
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
          sh '''
            kubectl --insecure-skip-tls-verify=true apply --validate=false -f k8s/namespace.yaml
            sed "s|IMAGE_PLACEHOLDER|$IMAGE_FULL|g" k8s/deployment.yaml | kubectl --insecure-skip-tls-verify=true apply --validate=false -f -
            kubectl --insecure-skip-tls-verify=true apply --validate=false -f k8s/service.yaml
            kubectl --insecure-skip-tls-verify=true rollout status deployment/java-app -n ci-cd-demo
          '''
        }
      }
    }
  }
}