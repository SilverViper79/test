def loadYaml(String filePath) {
    try {
        validateFile(filePath)
        def parsedYaml = readYaml file: filePath
        echo "YAML file is valid and has been read successfully."
        echo "YAML Content: ${parsedYaml}"
        return parsedYaml
    } catch (Exception e) {
        error "YAML syntax error in file: ${filePath}\n${e.message}"
    }
}

def validateFile(String filePath) {
    try {
        if (!fileExists(filePath)) {
            error "File not found: ${filePath}"
        }
    } catch (Exception e) {
        error "YAML syntax error in file: ${filePath}\n${e.message}"
    }
}

def setEnvVarsFromYaml(String filePath) {
    loadYaml(filePath)
    def envVars = readYaml file: filePath
    envVars.each { key, value ->
        env[key] = value
    }
}


def helmLint(String helmChartPath) {
    try {
        sh "ls -ltr"
        sh "/usr/local/bin/helm lint ${helmChartPath}"
        echo "Helm linting completed successfully."
    } catch (Exception e) {
        error "Helm lint failed for chart at ${helmChartPath}.\n${e.message}"
    }
}

def k8sApply(String helmChartPath, String releaseName, String namespace = 'default', String valuesFile = null, boolean dryRun = false, boolean atomic = false, boolean wait = false, String timeout = '5m', boolean force = false) {
    try {
        sh """
            sh "ls -ltr"
            sh "/usr/local/bin/helm lint ${helmChartPath}"
            helm upgrade --install ${releaseName} ${helmChartPath} \
            --namespace ${namespace} \
            ${valuesFile ? "--values ${valuesFile}" : ""} \
            ${dryRun ? "--dry-run" : ""} \
            ${atomic ? "--atomic" : ""} \
            ${wait ? "--wait" : ""} \
            ${timeout ? "--timeout ${timeout}" : ""} \
            ${force ? "--force" : ""}
        """
        echo "Helm upgrade/install ${dryRun ? 'dry-run' : 'completed'} successfully for release: ${releaseName}."
    } catch (Exception e) {
        error "Helm upgrade/install failed for release: ${releaseName}.\n${e.message}"
    }
}