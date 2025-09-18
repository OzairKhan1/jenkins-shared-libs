def call(String gitUrl, String branch, String credentialsId, 
         String commitMessage = "Automated commit from Jenkins", 
         boolean force = false, boolean rebase = false) {

    sh """
        git config user.email "ozairk048@gmail.com"
        git config user.name "OzairKhan1"
        git add .
        git diff --cached --quiet || git commit -m "${commitMessage}" || echo "No changes to commit"
    """

    if (rebase) {
        sh "git pull origin ${branch} --rebase || true"
    }

    def pushCmd = force ? "git push --force origin ${branch}" : "git push origin ${branch}"

    withCredentials([usernamePassword(credentialsId: credentialsId,
                                      usernameVariable: 'GIT_USERNAME',
                                      passwordVariable: 'GIT_PASSWORD')]) {
        withEnv(["AUTHED_URL=https://${GIT_USERNAME}:${GIT_PASSWORD}@${gitUrl.replaceFirst(/^https?:\\/\\//, '')}"]) {
            sh "${pushCmd.replace('origin', '\$AUTHED_URL')}"
        }
    }
}
