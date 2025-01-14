#!/bin/bash

KEY_DIR="src/main/resources/certs"
GITIGNORE=".gitignore"


if [ ! -d "$KEY_DIR" ]; then
  mkdir -p "$KEY_DIR"
fi

cd "$KEY_DIR" || exit


openssl genrsa -out keypair.pem 2048

openssl rsa -in keypair.pem -pubout -out public-key.pem

openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private-key.pem

rm keypair.pem

cd - || exit

if [ -f "$GITIGNORE" ]; then
  if ! grep -q "$KEY_DIR/" "$GITIGNORE"; then
    echo "$KEY_DIR/" >> "$GITIGNORE"
  fi
fi
