#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <service-name>"
  exit 1
fi

SERVICE_NAME=$1

ENV_FILE="${SERVICE_NAME}/.env.dev"
if [ ! -f "$ENV_FILE" ]; then
  echo "Error: Environment file $ENV_FILE not found!"
  exit 1
fi
export $(cat "$ENV_FILE" | xargs)


APP_VERSION=1.0-SNAPSHOT

java -jar "${SERVICE_NAME}/target/${SERVICE_NAME}-${APP_VERSION}.jar"