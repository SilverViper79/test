def call() {
    node {
        try {
            stage('Checkout') {
                echo "Starting Workflow"
                cleanWs()
                sh "git clone https://github.com/trainingdevsecops/Dummy.git"
                echo "---"
            }

            stage('Load Configuration') {
                def pipelineConfigPath = "Dummy/.pipeline/config.yaml"
                def envFilePath = 'Dummy/config/env.yaml'
                def helmConfigPath = 'Dummy/config/values.yaml'
                def pipelineMetadata = config(pipelineConfigPath)
                echo "Pipeline Metadata: ${pipelineMetadata}"
            }

            stage('Helm Lint') {
                echo "Running Helm Lint"
                // Use pipelineMetadata or other stages as needed
            }

            stage('Deploy') {
                echo "Running Deployment"
                // Deploy logic goes here
            }

        } catch (Exception e) {
            currentBuild.result = 'FAILURE'
            throw e
        }
    }
}
