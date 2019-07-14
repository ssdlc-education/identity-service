FROM maven:3.6.1-jdk-8

WORKDIR /tmp
RUN curl https://releases.hashicorp.com/vault/1.1.3/vault_1.1.3_linux_amd64.zip --output vault.zip
RUN unzip vault.zip
RUN mv /tmp/vault /usr/bin/
ENV VAULT_ADDR http://127.0.0.1:8200
COPY init-vault.sh /tmp/
CMD /tmp/init-vault.sh && sleep infinity
