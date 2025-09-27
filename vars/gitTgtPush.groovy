def call(String gitUrl, String branch, String credentialsId = null, String commitMessage = "Automated commit from Jenkins") {
    echo "Preparing to push changes to ${gitUrl} on branch ${branch}..."

    // Decide which remote to use (origin or target)
    def remoteName = "origin"
    sh """
        git config user.email "ozairk048@gmail.com"
        git config user.name "OzairKhan1"

        # Get current origin URL (if exists)
        current_origin=\$(git remote get-url origin || true)

        if [ "\$current_origin" = "${gitUrl}" ]; then
            echo "Origin already points to ${gitUrl}, reusing origin."
        else
            echo "Origin != ${gitUrl}, configuring remote 'target'."
            if git remote | grep -q target; then
                git remote set-url target ${gitUrl}
            else
                git remote add target ${gitUrl}
            fi
            echo "target" > .remote_name   # save remote name for later
        fi
    """

    // Read remote name set in shell (default origin, overridden to target if needed)
    remoteName = sh(returnStdout: true, script: 'if [ -f .remote_name ]; then cat .remote_name; else echo origin; fi').trim()

    if (credentialsId) {
        withCredentials([usernamePassword(credentialsId: credentialsId,
                                          usernameVariable: 'GIT_USERNAME',
                                          passwordVariable: 'GIT_PASSWORD')]) {
sh """
        git branch -M ${branch}
        git pull origin ${branch} --no-edit || true
        git add .
        git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
        git push origin ${branch}
"""

        }
    } else {
sh """
            git branch -M ${branch}
            git pull origin ${branch} --no-edit || true
            git add .
            git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
            git push origin ${branch}
"""

    }
}
