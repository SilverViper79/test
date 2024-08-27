def helm(Map pipelineMetadata) {
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
        echo "Helm Values Config: ${pipelineMetadata.helmVaulesConfig}"  // Correct the reference to pipelineMetadata
        // helmlint(pipelineMetadata)  // Uncomment when helmlint is implemented
    }

    stage('Deploy') {
        echo "Deploy Stage"
        echo "Helm Values Config: ${pipelineMetadata.helmVaulesConfig}"  // Correct the reference to pipelineMetadata
        // helmapply(pipelineMetadata)  // Uncomment when helmapply is implemented
    }
}
