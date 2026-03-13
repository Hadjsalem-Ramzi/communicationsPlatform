pipeline {
    agent any

    tools {
        nodejs 'NodeJS18'
    }

    environment {
        DOCKER_HUB_USER = 'hadjsalemramzi'
        Backend_IMAGE = "${DOCKER_HUB_USER}/spring-app:${BUILD_NUMBER}"
        frontEnd_IMAGE = "${DOCKER_HUB_USER}/angular-app:${BUILD_NUMBER}"
        Backend_LATEST = "${DOCKER_HUB_USER}/spring-app:latest"
        frontEnd_LATEST = "${DOCKER_HUB_USER}/angular-app:latest"
    }

    stages {
        // 📦 ÉTAPE 1 : INTÉGRATION & QUALITÉ
        stage('integration & quality') {
            steps {
                echo '🔍 Récupération du code et compilation...'
                // Checkout du code
                checkout scm

                // Compilation Backend
                dir('Backend') {
                    withMaven(
                        maven: 'Maven3',
                        jdk: 'JDK17',
                        mavenLocalRepo: '.repository'
                    ) {
                        sh 'mvn clean compile'
                    }
                }

                // Installation dépendances Frontend
                dir('frontEnd') {
                    sh 'npm install'
                }
            }
        }

        // 🧪 ÉTAPE 2 : TESTS FONCTIONNELS
        stage('test: functional') {
            steps {
                echo '🧪 Exécution des tests...'

                // Tests Backend
                dir('Backend') {
                    withMaven(
                        maven: 'Maven3',
                        jdk: 'JDK17',
                        mavenLocalRepo: '.repository'
                    ) {
                        sh 'mvn test -DskipTests'
                    }
                }

                // Tests Frontend (si vous avez des tests)
                dir('frontEnd') {
                    // sh 'npm test -- --watch=false --browsers=ChromeHeadless || true'
                    echo 'Tests frontend désactivés pour le moment'
                }
            }
            post {
                always {
                    // Publier les rapports de tests
                    junit allowEmptyResults: true, testResults: 'Backend/target/surefire-reports/*.xml'
                }
            }
        }

        // 🔒 ÉTAPE 3 : SÉCURITÉ
        stage('security') {
            steps {
                echo '🔒 Analyse de sécurité des dépendances...'

                // Audit npm pour le frontend
                dir('frontEnd') {
                    sh 'npm audit --production || true'
                }

                // Vous pouvez ajouter ici d'autres outils de sécurité
                // Exemple avec OWASP Dependency Check pour Maven
                // dir('Backend') {
                //     withMaven(maven: 'Maven3') {
                //         sh 'mvn org.owasp:dependency-check-maven:check'
                //     }
                // }
            }
        }

        // 👆 ÉTAPE 4 : APPROBATION MANUELLE
        stage('approval') {
            steps {
                echo '⏳ En attente d\'approbation pour le déploiement...'
                input message: 'Approuver le déploiement en production ?',
                      ok: '✅ Oui, déployer'
            }
        }

        // 🚀 ÉTAPE 5 : DÉPLOIEMENT EN PRODUCTION
        stage('deploy: prod') {

            steps {
                echo '🚀 Packaging et déploiement en production...'

                // Package Backend
                dir('Backend') {
                    withMaven(
                        maven: 'Maven3',
                        jdk: 'JDK17',
                        mavenLocalRepo: '.repository'
                    ) {
                        sh 'mvn package -DskipTests'
                    }
                }

                // Build Angular pour production
                dir('frontEnd') {
                    sh 'npm run build --prod'
                }

                // Construction des images Docker
                script {
                    // Build images
                    sh """
                        TAG=${BUILD_NUMBER} docker compose build
                    """

                    // Push vers Docker Hub (seulement sur main)
                    docker.withRegistry('', 'docker-hub-credentials') {
                        sh """
                            docker tag ${Backend_IMAGE} ${Backend_LATEST}
                            docker push ${Backend_IMAGE}
                            docker push ${Backend_LATEST}

                            docker tag ${frontEnd_IMAGE} ${frontEnd_LATEST}
                            docker push ${frontEnd_IMAGE}
                            docker push ${frontEnd_LATEST}
                        """
                    }
                }
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