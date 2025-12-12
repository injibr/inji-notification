automata {

    skipHom = true

    build.agent.image = 'library/maven:3.9-eclipse-temurin-21'

    //kustomization not ready
    //gitOps.provider = 'GIT_INFRA'     
    //gitOps.namespace = 'inji'     
    //gitOps.repos = [dev: 'gitops-np/inji']

    containers.add descriptor: 'Dockerfile', imageName: 'inji/inji-notification'

    artifacts.add file: 'target/notification-service-${version}.jar'

    build.opts = "-Dgpg.skip=true -Dmaven.javadoc.skip=true"

}
