package com.yahoo.identity.services.key;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyServiceUtils {
    private static final String CMD_READ_PUBLIC_KEY = "vault read -format=json transit/keys/";
    private static final String CMD_READ_PRIVATE_KEY = "vault read -format=json transit/export/signing-key/";

    private static JSONObject getStdoutFromExec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            int rc = process.waitFor();
            if (rc != 0) {
                throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                            "Exec returned a non-zero code.",
                                            new Throwable(IOUtils.toString(process.getErrorStream(), "UTF-8")));
            }
            return new JSONObject(new JSONTokener(process.getInputStream()));

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

    private static byte[] parsePEM(byte[] PEMBytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(PEMBytes);
        PemReader reader = new PemReader(new InputStreamReader(inputStream, "UTF-8"));
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
            .getJSONObject("data")
            .getJSONObject("keys")
            .getJSONObject("1")
            .getString("public_key");
        byte[] bytes = KeyServiceUtils.parsePEM(keyStr.getBytes("UTF-8"));
        return KeyServiceUtils.getPublicKey(bytes, algorithm);
    }

    public static PrivateKey readPrivateKeyFromVault(String keyName, String algorithm) throws IOException {
        String keyStr = getStdoutFromExec(CMD_READ_PRIVATE_KEY + keyName)
            .getJSONObject("data")
            .getJSONObject("keys")
            .getString("1");
        byte[] bytes = KeyServiceUtils.parsePEM(PKCS1ToPKCS8(keyStr.getBytes("UTF-8")));
        return KeyServiceUtils.getPrivateKey(bytes, algorithm);
    }
}
