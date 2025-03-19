#!/bin/bash

KONG_ADMIN="http://localhost:8001"

# Function to get IP address for both Linux and macOS
get_ip_address() {
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        CURRENT_IP=$(ipconfig getifaddr en0 || ipconfig getifaddr en1)
    else
        # Linux
        CURRENT_IP=$(hostname -I | awk '{print $1}')
    fi
}

get_ip_address

if [ -z "$CURRENT_IP" ]; then
    echo "Error: Could not determine IP address"
    exit 1
fi

echo "Detected IP address: $CURRENT_IP"

SERVICES=$(curl -s -X GET "$KONG_ADMIN/services" | jq -r '.data[].name')

for SERVICE_NAME in $SERVICES; do
    curl -s -o /dev/null -X PATCH "$KONG_ADMIN/services/$SERVICE_NAME" \
        --data "host=$CURRENT_IP"
    echo "Updated service $SERVICE_NAME to host $CURRENT_IP"
done