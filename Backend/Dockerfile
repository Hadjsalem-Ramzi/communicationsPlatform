# Utiliser une image de base OpenJDK
FROM openjdk:17-jdk-alpine

# Créer un répertoire pour les fichiers temporaires de Spring Boot
VOLUME /tmp

# Copier le fichier JAR dans l'image Docker
COPY target/PlateformCommunication-0.0.1-SNAPSHOT.jar BackCommunication.jar

# Définir le fuseau horaire pour le backend
ENV TZ=Europe/Paris
RUN apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Exposer le port 8083
EXPOSE 8083

# Lancer l'application avec Java
ENTRYPOINT ["java", "-jar", "/BackCommunication.jar"]
