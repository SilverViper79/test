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
        def helmChartPath = "Dummy/charts/${env.CHART_NAME}"
        utils.helmLint(helmChartPath)
    }

    stage('Deploy') {
        echo "Deploy Stage"
        def helmChartPath = "Dummy/charts/${env.CHART_NAME}"
        utils.helmApply(helmChartPath, 'hello-world')
    }
}
