def setEnvVarsFromYaml(String envFile) {
    def envVars = readYaml file: envFile
    envVars.each { key, value ->
        // Set each key-value pair as an environment variable
        env[key] = value
    }
}

def call(){
    def pipelineMetadata = stageConfig()
    def envFilePath = 'config/env.yaml'
    def helmConfigPath = 'config/values.yaml'
    try {
        if (fileExists(envFilePath)) {
            echo "Reading environment variables from ${envFilePath}"
            setEnvVarsFromYaml(envFilePath)
        } else {
            error "Environment file not found: ${envFilePath}"
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