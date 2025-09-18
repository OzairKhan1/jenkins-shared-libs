def call(String imageName, String credentialsId, String registry = "https://index.docker.io/v1/") {
    echo "Pushing Docker image: ${imageName}"

    withCredentials([usernamePassword(credentialsId: credentialsId,
                                      usernameVariable: 'DOCKER_USER',
                                      passwordVariable: 'DOCKER_PASS')]) {
        sh """
            echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin ${registry}
            docker push ${imageName}
            docker logout ${registry}
        """
    }
}
