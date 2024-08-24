def setEnvVarsFromYaml(String envFile) {
    def envVars = readYaml file: envFile
    envVars.each { key, value ->
        // Set each key-value pair as an environment variable
        env[key] = value
    }
}

def call(){
    def pipelineMetadata = stageConfig(config)
    def envFilePath = 'config/env.yaml'
    def helmConfigPath = 'config/values.yaml'
    try {
        if (fileExists(envFilePath)) {
            setEnvVarsFromYaml(envFilePath)
        } else {
            pipelineLogger.error("Environment file not found: ${envFilePath}")
        }

        if (!fileExists(helmConfigPath)) {
            pipelineLogger.error("Helm config file not found: ${helmConfigPath}.")
        }

        if(pipelineMetadata.stage?.deploy) {

            if(pipelineMetadata.stage?.deploy?.k8s) {
                stageK8s(pipelineMetadata)
            }

            if(pipelineMetadata.stage?.deploy?.lambda) {
                echo "Lambda"
            }
        }
    } catch (Exception exception) {
        throw exception
    }
}