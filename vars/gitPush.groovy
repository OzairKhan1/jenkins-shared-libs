def call(String gitUrl, String branch, String credentialsId = null, String commitMessage = "Automated commit from Jenkins") {
    echo "Pushing to ${gitUrl} on branch ${branch}..."

    sh """
        git config user.email "ozairk048@gmail.com"
        git config user.name "OzairKhan1"
        # Ensure remote is set
        if ! git remote | grep -q origin; then
            git remote add origin ${gitUrl}
        else
            git remote set-url origin ${gitUrl}
        fi
    """

    if (credentialsId) {
        withCredentials([usernamePassword(credentialsId: credentialsId,
                                          usernameVariable: 'GIT_USERNAME',
                                          passwordVariable: 'GIT_PASSWORD')]) {
            sh """
                git branch -M ${branch}
                git pull https://${GIT_USERNAME}:${GIT_PASSWORD}@${gitUrl.replaceFirst(/^https?:\\/\\//, '')} ${branch} --rebase || true
                git add .
                git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
                git push https://${GIT_USERNAME}:${GIT_PASSWORD}@${gitUrl.replaceFirst(/^https?:\\/\\//, '')} ${branch}
            """
        }
    } else {
        sh """
            git branch -M ${branch}
            git pull origin ${branch} --rebase || true
            git add .
            git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
            git push origin ${branch}
        """
    }
}
