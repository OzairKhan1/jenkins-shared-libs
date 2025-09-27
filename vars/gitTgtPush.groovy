def call(String gitUrl, String branch, String credentialsId = null, String commitMessage = "Automated commit from Jenkins") {
    echo "Pushing to ${gitUrl} on branch ${branch}..."

    sh """
        git config user.email "ozairk048@gmail.com"
        git config user.name "OzairKhan1"
        # Add/Update secondary remote (not touching 'origin')
        if git remote | grep -q target; then
            git remote set-url target ${gitUrl}
        else
            git remote add target ${gitUrl}
        fi
    """

    if (credentialsId) {
        withCredentials([usernamePassword(credentialsId: credentialsId,
                                          usernameVariable: 'GIT_USERNAME',
                                          passwordVariable: 'GIT_PASSWORD')]) {
            sh """
                git fetch target ${branch} || true
                git checkout -B ${branch} target/${branch} || git checkout -B ${branch}
                git add .
                git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
                git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${gitUrl.replaceFirst(/^https?:\\/\\//, '')} ${branch}
            """
        }
    } else {
        sh """
            git fetch target ${branch} || true
            git checkout -B ${branch} target/${branch} || git checkout -B ${branch}
            git add .
            git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
            git push target ${branch}
        """
    }
}
