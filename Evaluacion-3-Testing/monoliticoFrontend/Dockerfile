# Etapa base para pasos comunes
FROM node:22 as base
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# Etapa de desarrollo
FROM base as dev
CMD ["npm", "run", "dev"]

# Etapa de construcción
FROM base as build
ARG VITE_API_URL=http://localhost/api
ARG VITE_KEYCLOAK_URL=http://localhost:8080
ARG VITE_KEYCLOAK_REALM=toolRent
ARG VITE_KEYCLOAK_CLIENT_ID=toolRent-frontend
ENV VITE_API_URL=$VITE_API_URL
ENV VITE_KEYCLOAK_URL=$VITE_KEYCLOAK_URL
ENV VITE_KEYCLOAK_REALM=$VITE_KEYCLOAK_REALM
ENV VITE_KEYCLOAK_CLIENT_ID=$VITE_KEYCLOAK_CLIENT_ID
RUN npm run build

# Etapa de producción
FROM nginx:alpine
# 🔥 BORRA el default de nginx
RUN rm /etc/nginx/conf.d/default.conf

# 🔥 COPIA EL TUYO
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]