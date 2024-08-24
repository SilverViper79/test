def setEnvVarsFromYaml(String envFile) {
    def envVars = readYaml file: envFile
    envVars.each { key, value ->
        // Set each key-value pair as an environment variable
        env[key] = value
    }
}

def stageConfig(){
    def pipelineMetadata
    try{
        stage('init - pipeline'){
            pipelineMetadata = pipelineConfig()

            return pipelineMetadata
        }
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}

def pipelineConfig(pipelineConfig = ".pipeline/config.yaml"){
//    utilFile.checkFile(pipelineConfig, "fatal")

    def config = readYaml file: pipelineConfig

    return config
}

def stages(Map pipelineMetadata) {
    stage('Helm Lint') {
        echo "Lint"
        helmlint(pipelineMetadata)
    }

    stage('Deploy') {
        echo "Deploy"
        helmapply(pipelineMetadata)
    }
}

def stageK8s(Map pipelineMetadata){
    try{
        stages(pipelineMetadata)
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}


def call(){
    node {
        checkout scm
        def pipelineMetadata = stageConfig()
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

            if (pipelineMetadata.stage?.deploy) {

                if (pipelineMetadata.stage?.deploy?.k8s) {
                    stageK8s(pipelineMetadata)
                }

                if (pipelineMetadata.stage?.deploy?.lambda) {
                    echo "Lambda"
                }
            }
        } catch (Exception exception) {
            throw exception
        }
    }
}
