def stages(Map pipelineMetadata) {
    stage('Helm Lint') {
        utilK8s.lint(pipelineMetadata)
    }

    stage('Deploy') {
        utilK8s.apply(pipelineMetadata)
    }
}

def call(Map pipelineMetadata){
    try{
        stages(pipelineMetadata)
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}

