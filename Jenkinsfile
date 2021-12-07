pipeline {
    agent any
    tools{
        maven '3.6.3'
    }

    environment {
        // This can be nexus3 or nexus2
        NEXUS_VERSION = "nexus3"
        // This can be http or https
        NEXUS_PROTOCOL = "https"
        // Where your Nexus is running
        NEXUS_URL = "nexus.haeger-consulting.de"
        // Repository where we will upload the artifact
        NEXUS_REPOSITORY = "Training-Maven"
        // Jenkins credential id to authenticate to Nexus OSS
        NEXUS_CREDENTIAL_ID = "Training-Nexus-Uploader"
        // Nexus docker Repository
        NEXUS_DOCKER_URL_REPOSITORY = "https://nexus.haeger-consulting.de/repository/Training-Docker/"
        // Docker image name
        DOCKER_IMAGE_NAME = "docker_apotheke_stoller"
        // Dcoker image container
        dockerImage = ""
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building the application'
                sh 'mvn clean compile'
                //archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                echo 'Testing the application'
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying the application'

                sh 'mvn install'

                 script {
                     // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                    pom = readMavenPom file: "pom.xml";
                    // Find built artifact under target folder
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    // Print some info from the artifact found
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    // Extract the path from the File found
                    artifactPath = filesByGlob[0].path;
                    // Assign to a boolean response verifying If the artifact name exists
                    artifactExists = fileExists artifactPath;

                     if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                // Artifact generated such as .jar, .ear and .war files.
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],

                                // Lets upload the pom.xml file for additional information for Transitive dependencies
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );

                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }

                 }
            }
        }
        stage('Docker_build_image')
        {
            steps
            {
                script
                {
                    dockerImage = docker.build DOCKER_IMAGE_NAME
                }
            }
        }
        stage('Upload_docker_image_nexus')
        {
            steps
            {
                script
                {
                    docker.withRegistry( 'http://'+NEXUS_DOCKER_URL_REPOSITORY, NEXUS_CREDENTIAL_ID )
                    {
                        dockerImage.push('latest')
                    }
                }
            }
        }
    }
}
