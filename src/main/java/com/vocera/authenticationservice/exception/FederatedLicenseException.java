/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.exception;

/**
 * Federated License Exception.
 *
 * @author Rohit Phatak
 */
public class FederatedLicenseException extends RuntimeException {

    public FederatedLicenseException(String message) {
        super(message);
    }
}
