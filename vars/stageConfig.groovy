def call(def config){
    def pipelineMetadata
    try{
        stage('init - pipeline'){
            pipelineMetadata = pipelineConfig.init(config)

            return pipelineMetadata
        }
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}