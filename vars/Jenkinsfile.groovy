def helmVaulesConfig // Declare the variable outside the stages

def setEnvVarsFromYaml(String envFile) {
    def envVars = readYaml file: envFile
    envVars.each { key, value ->
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

def pipelineConfig(pipelineConfig = "Dummy/.pipeline/config.yaml"){
    def config = readYaml file: pipelineConfig
    echo "${config}"
    return config
}

def stages(Map pipelineMetadata) {
    stage('Helm Lint') {
        echo "Lint"
        // helmlint(pipelineMetadata)
    }

    stage('Deploy') {
        echo "Deploy"
        // helmapply(pipelineMetadata)
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

def validateYamlFile(String filePath) {
    if (!fileExists(filePath)) {
        error "File not found: ${filePath}"
    }

    try {
        def parsedYaml = readYaml file: filePath
        echo "YAML file is valid and has been read successfully."
        echo "YAML Content: ${parsedYaml}"
        return parsedYaml
    } catch (Exception e) {
        error "YAML syntax error in file: ${filePath}\n${e.message}"
    }
}

def call(){
    node {
        cleanWs()
        sh "git clone https://github.com/trainingdevsecops/Dummy.git"
        def pipelineMetadata = stageConfig()
        def envFilePath = 'Dummy/config/env.yaml'
        def helmConfigPath = 'Dummy/config/values.yaml'
        try {
            def envConfig = validateYamlFile(envFilePath)
            def helmVaulesConfig = validateYamlFile(helmConfigPath)
            echo "${helmVaulesConfig}"
            echo "${helmVaulesConfig['namespace']}"

            if (fileExists(envFilePath)) {
                setEnvVarsFromYaml(envFilePath)
                echo "-------"
                sh "printenv"
                echo "-------"
            } else {
                echo "Environment file not found: ${envFilePath}"
            }

            if (!fileExists(helmConfigPath)) {
                echo "Helm config file not found: ${helmConfigPath}."
            }

            if (pipelineMetadata["pipeline"]["deploy"]["k8s"]) {
                echo "k8s"
                stageK8s(pipelineMetadata)
            }

            if (pipelineMetadata["pipeline"]["deploy"]["lambda"]) {
                echo "Lambda"
            }
        } catch (Exception exception) {
            throw exception
        }
    }
}
