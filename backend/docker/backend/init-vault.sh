#!/usr/bin/env bash

vault server -dev &
sleep 3
vault secrets enable transit
vault write transit/keys/identity-cookie type=rsa-2048 exportable=true
vault write transit/keys/identity-token type=rsa-2048 exportable=true
