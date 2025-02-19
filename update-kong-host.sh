#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <new-host-ip-or-hostname>"
  exit 1
fi

KONG_ADMIN="http://localhost:8001"
NEW_HOST="$1"

SERVICES=$(curl -s -X GET "$KONG_ADMIN/services" | jq -r '.data[].name')

for SERVICE_NAME in $SERVICES; do
  curl -s -o /dev/null -X PATCH "$KONG_ADMIN/services/$SERVICE_NAME" \
    --data "host=$NEW_HOST"
  echo "Updated service $SERVICE_NAME to new host $NEW_HOST"
done