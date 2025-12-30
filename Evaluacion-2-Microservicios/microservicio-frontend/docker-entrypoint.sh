#!/bin/sh

# Get configuration from environment variables with defaults
KEYCLOAK_URL=${REACT_APP_KEYCLOAK_URL:-http://keycloak:8080}
KEYCLOAK_REALM=${REACT_APP_KEYCLOAK_REALM:-toolRent}
KEYCLOAK_CLIENT_ID=${REACT_APP_KEYCLOAK_CLIENT_ID:-toolRent-Frontend}

# Create the configuration file
cat > /usr/share/nginx/html/keycloak-config.js << EOF
window.KEYCLOAK_CONFIG = {
  url: "$KEYCLOAK_URL",
  realm: "$KEYCLOAK_REALM",
  clientId: "$KEYCLOAK_CLIENT_ID"
};
EOF

echo "Keycloak config created:"
echo "  URL: $KEYCLOAK_URL"
echo "  Realm: $KEYCLOAK_REALM"
echo "  Client ID: $KEYCLOAK_CLIENT_ID"

# Start nginx
exec nginx -g "daemon off;"
