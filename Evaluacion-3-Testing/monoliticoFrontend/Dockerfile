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
RUN npm run build

# Etapa de producción
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]