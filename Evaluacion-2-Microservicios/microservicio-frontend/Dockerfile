# Use Node.js as the base image for building the frontend
FROM node:22-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application with default env vars
ENV NODE_ENV=production
ENV REACT_APP_KEYCLOAK_URL=http://keycloak:8080
ENV REACT_APP_KEYCLOAK_REALM=toolRent
ENV REACT_APP_KEYCLOAK_CLIENT_ID=toolRent-Frontend

RUN npm run build

# Use NGINX as the base image for serving the built application
FROM nginx:stable-alpine

# Copy the built application from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy the NGINX configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Create an entrypoint script that handles runtime configuration
RUN cat > /docker-entrypoint.sh << 'EOF'
#!/bin/sh
KEYCLOAK_URL=${REACT_APP_KEYCLOAK_URL:-http://keycloak:8080}
KEYCLOAK_REALM=${REACT_APP_KEYCLOAK_REALM:-toolRent}
KEYCLOAK_CLIENT_ID=${REACT_APP_KEYCLOAK_CLIENT_ID:-toolRent-Frontend}

cat > /usr/share/nginx/html/keycloak-config.js << INNER_EOF
window.KEYCLOAK_CONFIG = {
  url: "$KEYCLOAK_URL",
  realm: "$KEYCLOAK_REALM",
  clientId: "$KEYCLOAK_CLIENT_ID"
};
INNER_EOF

exec nginx -g "daemon off;"
EOF
RUN chmod +x /docker-entrypoint.sh

# Remove the default backup configuration file
RUN rm -f /etc/nginx/conf.d/default.conf.bak

# Expose the port where NGINX will serve the application
EXPOSE 80

# Use the entrypoint script
ENTRYPOINT ["/docker-entrypoint.sh"]