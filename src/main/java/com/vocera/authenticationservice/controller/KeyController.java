/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.controller;

import com.vocera.authenticationservice.service.KeyStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.logging.Logger;

/**
 * Key Controller for exposing public key.
 *
 * @author Rohit Phatak
 */
@RestController
public class KeyController {

    private static final Logger LOGGER = Logger.getLogger(KeyController.class.getName());

    private KeyStoreService keyStoreService;

    /**
     * Constructor.
     *
     * @param keyStoreService
     */
    public KeyController(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }

    /**
     * API for exposing public key.
     * Public key is formatted and returned.
     *
     * @return
     */
    @GetMapping("/token_key")
    public ResponseEntity<String> getPublicKey() {
        LOGGER.info("Get Public key called.");

        StringBuilder publicKey = new StringBuilder();
        publicKey.append("-----BEGIN PUBLIC KEY-----\n");
        publicKey.append(Base64.getEncoder().encodeToString(this.keyStoreService.getPublicKey().getEncoded()));
        publicKey.append("\n-----END PUBLIC KEY-----\n");
        return new ResponseEntity<>(publicKey.toString(), HttpStatus.OK);
    }
}
