def init(Map pipelineMetadata) {
    try {
        helmStages(pipelineMetadata)
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}

def helmStages(Map pipelineMetadata) {
    stage('Helm Lint') {
        echo "Helm Lint Stage"
        echo "Helm Values Config: ${env.CHART_NAME}"
        def helmChartPath = "Dummy/charts/${env.CHART_NAME}"
        helmLint(helmChartPath)
    }

    stage('Deploy') {
        echo "Deploy Stage"
        def helmChartPath = "Dummy/charts/${env.CHART_NAME}"
        echo "Helm Values Config: ${pipelineMetadata}"
        helmApply(helmChartPath, 'hello-world', dryRun = true)
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

def helmApply(String helmChartPath, String releaseName, String namespace = 'default', String valuesFile = null, boolean dryRun = false, boolean atomic = false, boolean wait = false, String timeout = '5m', boolean force = false) {
    try {
        sh """
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