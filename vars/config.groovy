def call(filePath) {
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

def pipelineConfig(path) {
    def config = readYaml file: path
    echo "${config}"
    return config
}