# Étape 1 : Build avec Maven
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copie des fichiers Maven et résolution des dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie du code source
COPY src ./src

# Compilation et packaging
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution légère
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copie du JAR construit
COPY --from=build /app/target/*.jar app.jar

# Exposition du port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]