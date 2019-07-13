#!/usr/bin/env bash

curl https://releases.hashicorp.com/vault/1.1.3/vault_1.1.3_linux_amd64.zip --output vault.zip
unzip vault.zip
./vault server -dev &
sleep 3
export VAULT_ADDR='http://127.0.0.1:8200'
./vault secrets enable transit
./vault write transit/keys/identity-cookie type=rsa-2048 exportable=true
./vault write transit/keys/identity-token type=rsa-2048 exportable=true
