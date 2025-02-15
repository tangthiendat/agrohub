#!/bin/bash

export $(cat product-service/.env.dev | xargs)

APP_VERSION=1.0-SNAPSHOT

java -jar product-service/target/product-service-${APP_VERSION}.jar