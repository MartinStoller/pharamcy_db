#stages: #define which stages there are in the pipeline
    #-maven_install
    #-unit_testing
variables:
        IMAGE_NAME: martinstoller/apotheke
        IMAGE_TAG: gitlab_pipe_2.0
run_unit_tests: #name of the job
    image: maven:3.8.4-openjdk-17 #docker image based on which the following script will be executed
    script: #list of comments that should be executed
        - mvn test


build_docker_image:
    image: docker:20.10.16 #we need docker in docker since we want to execute docker commands such as build and push inside our container
    services: # here we can list more images which are supposed to run in our container (additionally to the base image). In our case we need docker deamon in order to run docker commands
        - docker:20.10.16-dind
    variables:
        DOCKER_TLS_CERTDIR: "/certs" #tells docker to create certificates in that location so that both the client and the deamon (aka the 2 docker images) can communicate with each other.
    before_script:
        - docker login -u $REGISTRY_USER -p $REGISTRY_PASS
    script:
        - docker build -t $IMAGE_NAME:$IMAGE_TAG .
        - docker push $IMAGE_NAME:$IMAGE_TAG
