package com.yahoo.identity.services.storage.sql;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.services.key.KeyServiceImpl;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CredentialModel{
    private Instant issueTime;
    private Instant expireTime;
    private String subject;
    private KeyServiceImpl keyServiceImpl = new KeyServiceImpl();

    public Instant getIssueTime() { return this.issueTime; }

    public void setIssueTime(@Nonnull Instant issueTime) { this.issueTime = issueTime; }

    public Instant getExpireTime() { return this.expireTime; }

    public void setExpireTime(@Nonnull Instant expireTime) { this.expireTime = expireTime; }

    public String getSubject() { return this.subject; }

    public void setSubject(@Nonnull String subject) { this.subject = subject; }

    public String toString(){
        try {
            Algorithm algorithm = Algorithm.HMAC256(keyServiceImpl.getSecret());
            String token = JWT.create()
                    .withExpiresAt(Date.from(expireTime))
                    .withIssuedAt(Date.from(issueTime))
                    .withSubject(subject)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            System.out.println("error: unable to create a token.");
            return null;
        }
    }

    public void fromString(@Nonnull String credStr) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(keyServiceImpl.getSecret());

            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)   // 1 sec for nbf and iat
                    .acceptExpiresAt(5)   // 5 secs for exp
                    .build();

            DecodedJWT jwt = verifier.verify(credStr);

            setSubject(jwt.getSubject());
            setIssueTime(jwt.getIssuedAt().toInstant());
            setExpireTime(jwt.getExpiresAt().toInstant());

            if (getIssueTime().compareTo(Instant.now().plus(-5, ChronoUnit.MINUTES))<0){
                throw new NotAuthorizedException("credential not issued in the past 5 minutes");
            }

        } catch(JWTVerificationException e){
            System.out.println(e.toString());
        }
    }
}
