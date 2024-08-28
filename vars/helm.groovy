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
        utils.helmLint(pipelineMetadata['helmChartPath'])
    }

    stage('Deploy') {
        echo "Deploy Stage"
        utils.helmApply(pipelineMetadata['helmChartPath'], 'hello-world', namespace = 'default', valuesFile = pipelineMetadata['helmConfigPath'], dryRun = pipelineMetadata['pipeline']['deploy']['k8s']['dry_run'])
    }
}
