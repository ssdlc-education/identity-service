# Write JWT secret for token and credential service
openssl genrsa -out .secret/cookie-private.key 512
openssl rsa -pubout -in .secret/cookie-private.key -out .secret/cookie-public.pem
openssl pkcs8 -topk8 -in .secret/cookie-private.key -inform pem -out .secret/cookie-private.pem -outform pem -nocrypt
chmod 400 .secret/cookie-private.pem
chmod 400 .secret/cookie-public.pem
rm .secret/cookie-private.key

openssl genrsa -out .secret/token-private.key 512
openssl rsa -pubout -in .secret/token-private.key -out .secret/token-public.pem
openssl pkcs8 -topk8 -in .secret/token-private.key -inform pem -out .secret/token-private.pem -outform pem -nocrypt
chmod 400 .secret/token-private.pem
chmod 400 .secret/token-public.pem
rm .secret/token-private.key