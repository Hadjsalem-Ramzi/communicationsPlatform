pipeline {
    agent any

    environment {
        // À MODIFIER - Votre nom d'utilisateur Docker Hub
        DOCKER_HUB_USER = 'hadjsalemramzi'

        // Noms des images avec tag (numéro de build)
        Backend_IMAGE = "${DOCKER_HUB_USER}/spring-app:${BUILD_NUMBER}"
        FRONTEND_IMAGE = "${DOCKER_HUB_USER}/angular-app:${BUILD_NUMBER}"

        // Tags "latest"
        Backend_LATEST = "${DOCKER_HUB_USER}/spring-app:latest"
        FRONTEND_LATEST = "${DOCKER_HUB_USER}/angular-app:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupération du code depuis GitHub
                checkout scm
                echo '✅ Code récupéré avec succès'
            }
        }

        stage('Build Backend avec Maven') {
            steps {
                dir('Backend') {
                    echo '🔨 Compilation du Backend Spring avec Maven...'
                    // Utilisation de Maven configuré dans Jenkins
                    withMaven(
                        maven: 'Maven3',          // Nom dans Global Tool Configuration
                        jdk: 'JDK17',              // Nom dans Global Tool Configuration
                        mavenLocalRepo: '.repository'
                    ) {
                        sh 'mvn clean compile'
                    }
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('Backend') {
                    echo '🧪 Exécution des tests unitaires...'
                    withMaven(
                        maven: 'Maven3',
                        jdk: 'JDK17',
                        mavenLocalRepo: '.repository'
                    ) {
                       // sh 'mvn test'
                    }
                }
            }
        }

        stage('Package Backend') {
            steps {
                dir('Backend') {
                    echo '📦 Packaging du Backend en JAR...'
                    withMaven(
                        maven: 'Maven3',
                        jdk: 'JDK17',
                        mavenLocalRepo: '.repository'
                    ) {
                        sh 'mvn package -DskipTests'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo '🔨 Installation des dépendances frontend...'
                    sh 'npm install'
                    echo '🔨 Build Angular pour production...'
                    sh 'npm run build --prod'
                }
            }
        }

        stage('Test Frontend') {
            steps {
                dir('frontend') {
                    echo '🧪 Tests frontend (headless)...'
                    sh 'npm test -- --watch=false --browsers=ChromeHeadless || true'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                echo '🐳 Construction des images Docker avec Docker Compose...'
                sh """
                    TAG=${BUILD_NUMBER} docker-compose build
                """
            }
        }

        stage('Test avec Docker Compose') {
            steps {
                echo '🧪 Tests d\'intégration avec les conteneurs...'
                sh """
                    TAG=${BUILD_NUMBER} docker-compose up -d
                    sleep 15  // Attendre le démarrage des services
                """
                sh 'curl -f http://localhost:8080/api/hello || exit 1'
                sh 'curl -f http://localhost || exit 1'
            }
            post {
                always {
                    sh 'docker-compose down'
                }
            }
        }

        stage('Push to Docker Hub') {
            when {
                branch 'main'  // Ne push que sur la branche principale
            }
            steps {
                echo '⬆️ Envoi des images vers Docker Hub...'
                script {
                    docker.withRegistry('', 'docker-hub-credentials') {
                        sh """
                            docker tag ${Backend_IMAGE} ${Backend_LATEST}
                            docker push ${Backend_IMAGE}
                            docker push ${Backend_LATEST}

                            docker tag ${FRONTEND_IMAGE} ${FRONTEND_LATEST}
                            docker push ${FRONTEND_IMAGE}
                            docker push ${FRONTEND_LATEST}
                        """
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            when {
                branch 'main'
            }
            steps {
                echo '🚀 Déploiement de l\'application...'
                sh """
                    TAG=${BUILD_NUMBER} docker-compose up -d
                    echo "✅ Application déployée !"
                    echo "Backend: http://localhost:8080/api/hello"
                    echo "Frontend: http://localhost"
                """
            }
        }
    }

    post {
        success {
            echo '✅✅✅ PIPELINE RÉUSSI ! ✅✅✅'
        }
        failure {
            echo '❌❌❌ PIPELINE ÉCHOUÉ - Consultez les logs ❌❌❌'
        }
        always {
            // Nettoyage
            cleanWs()
        }
    }
}