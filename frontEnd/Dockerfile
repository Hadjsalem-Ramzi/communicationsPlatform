# Étape 1 : Build avec Node.js
FROM node:18-alpine AS build
WORKDIR /app

# Copie des fichiers de dépendances
COPY package*.json ./
RUN npm ci

# Copie du code source
COPY . .

# Construction pour la production
RUN npm run build --prod

# Étape 2 : Serveur Nginx
FROM nginx:alpine

# Copie des fichiers compilés
COPY --from=build /app/dist/frontend /usr/share/nginx/html/

# Copie de la configuration Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposition du port
EXPOSE 80
