def init(filePath) {
    try {
        def pipelineMetadata = null
        stage('init - pipeline') {
            pipelineMetadata = pipelineConfig(filePath)
        }
        return pipelineMetadata
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}

def pipelineConfig(filePath) {
    def config = readYaml file: filePath
    echo "${config}"
    return config
}