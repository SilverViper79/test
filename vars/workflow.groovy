def call() {
    node {
        cleanWs()
        sh "git clone https://github.com/trainingdevsecops/Dummy.git"
        echo "---"
        def pipelineConfigPath = "Dummy/.pipeline/config.yaml"
        def envFilePath = 'Dummy/config/env.yaml'
        def helmConfigPath = 'Dummy/config/values.yaml'

        def pipelineMetadata = config(pipelineConfigPath)

        try {
            setEnvVarsFromYaml(envFilePath)
            parseYaml(helmConfigPath)
            echo "-------"
            sh "printenv"
            echo "-------"


            if (pipelineMetadata["pipeline"]["deploy"]["k8s"]) {
                echo "k8s"
                stage(pipelineMetadata)
            }

            if (pipelineMetadata["pipeline"]["deploy"]["lambda"]) {
                echo "Lambda"
            }
        } catch (Exception exception) {
            throw exception
        }
    }
}
