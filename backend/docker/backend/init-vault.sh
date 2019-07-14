#!/usr/bin/env bash

vault server -dev &
sleep 3
# See https://www.vaultproject.io/docs/secrets/transit/index.html
# https://www.vaultproject.io/api/secret/transit/index.html
vault secrets enable transit
vault write transit/keys/identity-cookie type=rsa-2048 exportable=true
vault write transit/keys/identity-token type=rsa-2048 exportable=true

# The keys can be read from command line like below:
# vault read -format=json transit/export/hmac-key/identity-cookie
# vault read -format=json transit/export/signing-key/identity-cookie
# vault read -format=json transit/export/encryption-key/identity-cookie
# vault read -format=json transit/keys/identity-cookie
