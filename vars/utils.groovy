def parseYaml(String filePath) {
    try {
        fileExists(filePath)
        def parsedYaml = readYaml file: filePath
        echo "YAML file is valid and has been read successfully."
        echo "YAML Content: ${parsedYaml}"
        return parsedYaml
    } catch (Exception e) {
        error "YAML syntax error in file: ${filePath}\n${e.message}"
    }
}

def fileExists(String filePath) {
    if (!fileExists(filePath)) {
        error "File not found: ${filePath}"
    }
}

def setEnvVarsFromYaml(String filePath) {
    parseYaml(filePath)
    def envVars = readYaml file: envFile
    envVars.each { key, value ->
        env[key] = value
    }
}