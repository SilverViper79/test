def init(filePath) {
    try{
        stage('init - pipeline'){
            def pipelineMetadata = pipelineConfig(filePath)
            return pipelineMetadata
        }
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