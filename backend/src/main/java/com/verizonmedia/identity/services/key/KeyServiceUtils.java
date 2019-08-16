// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.key;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyServiceUtils {
    private static final String CMD_READ_PUBLIC_KEY = "vault read -format=json transit/keys/";
    private static final String CMD_READ_PRIVATE_KEY = "vault read -format=json transit/export/signing-key/";
    private static final String CMD_READ_HMAC_KEY = "vault read -format=json transit/export/hmac-key/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static JsonNode getStdoutFromExec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            int rc = process.waitFor();
            if (rc != 0) {
                throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                            "Exec returned a non-zero code.",
                                            new Throwable(IOUtils.toString(process.getErrorStream(), "UTF-8")));
            }
            return objectMapper.readTree(process.getInputStream());

        } catch (java.io.IOException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Exec failed due to I/O error.",
                                        e);
        } catch (java.lang.InterruptedException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "The thread was interrupted when waiting for child process to finish execution.",
                                        e);
        }
    }

    private static byte[] PKCS1ToPKCS8(byte[] PKCS1Bytes) {
        try {
            Process process = Runtime.getRuntime().exec("openssl pkcs8 -topk8 -nocrypt");

            OutputStream outputStream = process.getOutputStream();
            outputStream.write(PKCS1Bytes);
            outputStream.flush();
            outputStream.close();

            int rc = process.waitFor();
            if (rc != 0) {
                throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                            "Exec returned a non-zero code.",
                                            new Throwable(IOUtils.toString(process.getErrorStream(), "UTF-8")));
            }

            return IOUtils.toByteArray(process.getInputStream());

        } catch (java.io.IOException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Exec failed due to I/O error.",
                                        e);
        } catch (java.lang.InterruptedException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "The thread was interrupted when waiting for child process to finish execution.",
                                        e);
        }
    }

    private static byte[] parsePEM(byte[] pemBytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(pemBytes);
        PemReader reader = new PemReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        PemObject pemObject = reader.readPemObject();
        reader.close();
        return pemObject.getContent();
    }

    private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) {
        PublicKey publicKey;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Could not reconstruct the public key, the given algorithm could not be found.",
                                        e);
        } catch (InvalidKeySpecException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Could not reconstruct the public key.", e);
        }

        return publicKey;
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
        PrivateKey privateKey;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Could not reconstruct the private key, the given algorithm could not be found.",
                                        e);
        } catch (InvalidKeySpecException e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Could not reconstruct the private key.", e);
        }

        return privateKey;
    }

    public static PublicKey readPublicKeyFromVault(String keyName, String algorithm) throws IOException {
        String keyStr = getStdoutFromExec(CMD_READ_PUBLIC_KEY + keyName)
            .get("data")
            .get("keys")
            .get("1")
            .get("public_key")
            .asText();
        byte[] bytes = parsePEM(keyStr.getBytes(StandardCharsets.UTF_8));
        return getPublicKey(bytes, algorithm);
    }

    public static PrivateKey readPrivateKeyFromVault(String keyName, String algorithm) throws IOException {
        String keyStr = getStdoutFromExec(CMD_READ_PRIVATE_KEY + keyName)
            .get("data")
            .get("keys")
            .get("1")
            .asText();
        byte[] bytes = parsePEM(PKCS1ToPKCS8(keyStr.getBytes(StandardCharsets.UTF_8)));
        return getPrivateKey(bytes, algorithm);
    }

    public static byte[] readHMACFromVault(String keyName) {
        String encodedKeyStr = getStdoutFromExec(CMD_READ_HMAC_KEY + keyName)
            .get("data")
            .get("keys")
            .get("1")
            .asText();
        return Base64.getDecoder().decode(encodedKeyStr);
    }
}
