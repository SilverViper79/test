def log(config){
    log("\n${utilMap.printNestedMap(config, "\t- ")}", "info")
}

def call(pipelineConfig = ".pipeline/config.yaml"){
    utilFile.checkFile(pipelineConfig, "fatal")

    def config = readYaml file: pipelineConfig

    log("${config}", "debug")
    return config
}