package com.yahoo.identity.services.key;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Nonnull;
import javax.ws.rs.InternalServerErrorException;

public class KeyServiceImpl implements KeyService {

    private String secret;

    private static String readFileAsString(String fileName) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    @Override
    @Nonnull
    public String getSecret(@Nonnull String username) {
        try {
            this.secret = readFileAsString(".secret/" + username + ".key");
        } catch (FileNotFoundException e) {
            throw new InternalServerErrorException("Secret key file doesn't exists");
        } catch (Exception e) {
            throw new InternalServerErrorException("Unknown error occurs when reading file.");
        }
        return this.secret;
    }
}
