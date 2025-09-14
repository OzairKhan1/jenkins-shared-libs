def call(String gitUrl, String gitBranch, String credentialsId = null) {
    echo "Git Cloning from ${gitUrl} branch ${gitBranch}..."

    if (credentialsId) {
        // Clone with credentials
        git url: gitUrl, branch: gitBranch, credentialsId: credentialsId
    } else {
        // Clone without credentials
        git url: gitUrl, branch: gitBranch
    }
}
