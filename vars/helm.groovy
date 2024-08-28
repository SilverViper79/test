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
        utils.helmLint(helmChartPath)
    }

    stage('Deploy') {
        echo "Deploy Stage"
        def helmChartPath = "Dummy/charts/${env.CHART_NAME}"
        echo "Helm Values Config: ${pipelineMetadata}"
        utils.helmApply(helmChartPath, 'hello-world')
    }
}
