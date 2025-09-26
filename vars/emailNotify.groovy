def call(String recipients = 'ozairk048@gmail.com') {
    post {
        success {
            emailext(
                to: recipients,
                subject: "✅ SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """
                    <html>
                      <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: green;">✅ Jenkins Pipeline Success</h2>
                        <p>The pipeline <b>${env.JOB_NAME}</b> completed successfully.</p>
                        <p>
                          <b>Build Number:</b> ${env.BUILD_NUMBER}<br>
                          <b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                        </p>
                      </body>
                    </html>
                """,
                mimeType: 'text/html'
            )
        }

        failure {
            script {
                // Save full console log to a file
                def logFile = "console.log"
                writeFile file: logFile, text: currentBuild.rawBuild.getLog().join("\n")

                emailext(
                    to: recipients,
                    subject: "❌ FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                    body: """
                        <html>
                          <body style="font-family: Arial, sans-serif; color: #333;">
                            <h2 style="color: red;">❌ Jenkins Pipeline Failed</h2>
                            <p>The pipeline <b>${env.JOB_NAME}</b> has failed.</p>
                            <p>
                              <b>Build Number:</b> ${env.BUILD_NUMBER}<br>
                              <b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                            </p>
                            <p>The full console log is attached to this email for troubleshooting.</p>
                          </body>
                        </html>
                    """,
                    mimeType: 'text/html',
                    attachmentsPattern: 'console.log'
                )
            }
        }
    }
}
