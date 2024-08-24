def call(){
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