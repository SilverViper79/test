def call() {
    node {
        cleanWs()
        sh "git clone https://github.com/trainingdevsecops/Dummy.git"

        def pipelineConfigPath = "Dummy/.pipeline/config.yaml"


        def pipelineMetadata = metadata.init(pipelineConfigPath)
        pipelineMetadata["helmChartPath"] = 'examples/charts/hello-world'
        pipelineMetadata["envFilePath"] = 'Dummy/config/env.yaml'
        pipelineMetadata["helmConfigPath"] = 'Dummy/config/values.yaml'

        try {
            echo "-------"
            def envConfig = utils.setEnvVarsFromYaml(pipelineMetadata["envFilePath"])
            echo "----setEnvVarsFromYaml---"
            def helmConfig = utils.loadYaml(pipelineMetadata["helmConfigPath"])
            echo "----parseYaml---"
            sh "printenv"
            echo "-------"


            if (pipelineMetadata["pipeline"]["deploy"]["k8s"]) {
                echo "k8s"
                helm.init(pipelineMetadata)
            }

            if (pipelineMetadata["pipeline"]["deploy"]["lambda"]) {
                echo "Lambda"
            }
        } catch (Exception exception) {
            throw exception
        }
    }
}
