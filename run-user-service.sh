#!/bin/bash

export $(cat user-service/.env.dev | xargs)

APP_VERSION=1.0-SNAPSHOT

java -jar user-service/target/user-service-${APP_VERSION}.jar