/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.service;

import com.vocera.authenticationservice.entity.FederatedLicense;

/**
 * @author Rohit Phatak
 */
public interface FederatedLicenseService {

    /**
     * Check if the license key is already federated.
     *
     * @param licenseKey
     * @return
     */
    FederatedLicense checkLicense(String licenseKey);

    /**
     * Federate a new license key.
     *
     * @param licenseKey
     * @return
     */
    FederatedLicense federateLicense(String licenseKey);

    /**
     * Find by federation id(Also referred as organization id by other services).
     *
     * @param federationId
     * @return
     */
    FederatedLicense findByFederationId(Long federationId);
}
