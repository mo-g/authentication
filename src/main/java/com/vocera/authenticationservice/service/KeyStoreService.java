/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.logging.Logger;

/**
 * Service for handling key pair.
 *
 * @author Rohit Phatak
 */
@Service
public class KeyStoreService {

    private static final Logger LOGGER = Logger.getLogger(KeyStoreService.class.getName());

    @Value("${key-store.file}")
    private String keyFilePath;

    @Value("${key-store.password}")
    private String keyPassword;

    @Value("${key-store.key-alias}")
    private String keyAlias;

    /**
     * Get Key Pair.
     *
     * @return
     */
    public KeyPair getKeyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new FileSystemResource(keyFilePath), keyPassword.toCharArray());
        return keyStoreKeyFactory.getKeyPair(keyAlias);
    }

    /**
     * Get Public key.
     *
     * @return
     */
    public PublicKey getPublicKey() {
        return this.getKeyPair().getPublic();
    }
}
