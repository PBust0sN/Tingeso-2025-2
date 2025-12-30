# Use Node.js as the base image for building the frontend
FROM node:22-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

# Set environment variables for build
ARG REACT_APP_KEYCLOAK_URL=http://keycloak:8080
ARG REACT_APP_KEYCLOAK_REALM=toolRent
ARG REACT_APP_KEYCLOAK_CLIENT_ID=toolRent-Frontend

ENV NODE_ENV=production
ENV REACT_APP_KEYCLOAK_URL=${REACT_APP_KEYCLOAK_URL}
ENV REACT_APP_KEYCLOAK_REALM=${REACT_APP_KEYCLOAK_REALM}
ENV REACT_APP_KEYCLOAK_CLIENT_ID=${REACT_APP_KEYCLOAK_CLIENT_ID}

# Build the application
RUN npm run build

# Use NGINX as the base image for serving the built application
FROM nginx:stable-alpine

# Copy the built application from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy the NGINX configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Remove the default backup configuration file
RUN rm -f /etc/nginx/conf.d/default.conf.bak

# List the contents of the directory to verify the build output
RUN ls -la /usr/share/nginx/html/

# Expose the port where NGINX will serve the application
EXPOSE 80

# Command to start NGINX
CMD ["nginx", "-g", "daemon off;"]