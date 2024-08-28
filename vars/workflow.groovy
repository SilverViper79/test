def call() {
    node {
        cleanWs()
        sh "git clone https://github.com/trainingdevsecops/Dummy.git"

        // Define the path to the Helm chart
        def helmChartPath = 'examples/charts/hello-world'
        echo "---"
        def pipelineConfigPath = "Dummy/.pipeline/config.yaml"
//        def pipelineMetadata = readYaml file: pipelineConfigPath

        def envFilePath = 'Dummy/config/env.yaml'
        def helmConfigPath = 'Dummy/config/values.yaml'
//        def helmChartPath = 'src/hello-world'

        def pipelineMetadata = metadata.init(pipelineConfigPath)

        try {
            echo "-------"
            def envConfig = utils.setEnvVarsFromYaml(envFilePath)
            echo "----setEnvVarsFromYaml---"
            def helmConfig = utils.loadYaml(helmConfigPath)
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
