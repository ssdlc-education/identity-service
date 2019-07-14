FROM mysql:8

ENV MYSQL_RANDOM_ROOT_PASSWORD yes
ENV MYSQL_DATABASE identity
ENV MYSQL_USER identity

# Notice for production usage, the password should be stored in a secure place, e.g. HSM.
ENV MYSQL_PASSWORD insecure_password
COPY ./identity.sql /docker-entrypoint-initdb.d/

