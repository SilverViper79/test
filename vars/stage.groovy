def helm(Map pipelineMetadata){
    try{
        stages(pipelineMetadata)
    } catch (Exception exception) {
        currentBuild.result = 'FAILURE'
        throw exception
    }
}


def stages(Map pipelineMetadata) {
    stage('Helm Lint') {
        echo "Helm Lint Stage"
        echo "Helm Values Config: ${helmVaulesConfig}"  // Print the config during Helm Lint
//         helmlint(pipelineMetadata)
    }

    stage('Deploy') {
        echo "Deploy Stage"
        echo "Helm Values Config: ${helmVaulesConfig}"  // Print the config during Deploy
        // helmapply(pipelineMetadata)
    }
}