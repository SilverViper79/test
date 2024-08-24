


//def init(def cofnig){
//    pipelineEnv(config)
//    validateConfig(config)
//    if (!fileExists(env.PIPELINE_WORSPACE)){
//        sh "set +x; mkdir -p ${env.PIPELINE_WORSPACE}; set -x"
//    }
//
//    def pipelineMetadata = pipelineMetadata.compile(config)
//
//    return pipelineMetadata
//}