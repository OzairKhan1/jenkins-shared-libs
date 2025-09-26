def call(String version = "1.29.2") {
    echo "Installing docker-compose version: ${version}"

    sh """
        set -e
        sudo curl -L "https://github.com/docker/compose/releases/download/${version}/docker-compose-$(uname -s)-$(uname -m)" \
            -o /usr/local/bin/docker-compose
        sudo chmod +x /usr/local/bin/docker-compose
    """

    sh "docker-compose --version"
}
