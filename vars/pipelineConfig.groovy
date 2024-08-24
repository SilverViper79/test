def log(config){
    log("\n${utilMap.printNestedMap(config, "\t- ")}", "info")
}

def call(pipelineConfig = ".pipeline/config.yaml"){
    utilFile.checkFile(pipelineConfig, "fatal")

    def config = readYaml file: pipelineConfig

    log("${config}", "debug")
    return config
}


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