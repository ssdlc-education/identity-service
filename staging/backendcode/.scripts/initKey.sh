# Write JWT secret for token and credential service
openssl genrsa -out staging/backendcode/.secret/cookie-private.key 512
openssl rsa -pubout -in staging/backendcode/.secret/cookie-private.key -out .secret/cookie-public.pem
openssl pkcs8 -topk8 -in staging/backendcode/.secret/cookie-private.key -inform pem -out .secret/cookie-private.pem -outform pem -nocrypt
chmod 400 staging/backendcode/.secret/cookie-private.pem
chmod 400 staging/backendcode/.secret/cookie-public.pem
rm staging/backendcode/.secret/cookie-private.key

openssl genrsa -out staging/backendcode/.secret/token-private.key 512
openssl rsa -pubout -in staging/backendcode/.secret/token-private.key -out .secret/token-public.pem
openssl pkcs8 -topk8 -in staging/backendcode/.secret/token-private.key -inform pem -out .secret/token-private.pem -outform pem -nocrypt
chmod 400 staging/backendcode/.secret/token-private.pem
chmod 400 staging/backendcode/.secret/token-public.pem
rm staging/backendcode/.secret/token-private.key