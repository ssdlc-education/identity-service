#!/usr/bin/env bash

vault server -dev &
sleep 3
export VAULT_ADDR='http://127.0.0.1:8200'
vault secrets enable transit
vault write transit/keys/identity-cookie type=rsa-2048 exportable=true
vault write transit/keys/identity-token type=rsa-2048 exportable=true