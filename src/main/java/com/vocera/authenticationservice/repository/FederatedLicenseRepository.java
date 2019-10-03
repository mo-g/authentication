/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.repository;

import com.vocera.authenticationservice.entity.FederatedLicense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for accessing {@link FederatedLicense}.
 *
 * @author Rohit Phatak
 */
public interface FederatedLicenseRepository extends JpaRepository<FederatedLicense, Long> {

    /**
     * Find by license key.
     *
     * @param licenseKey
     * @return
     */
    Optional<FederatedLicense> findByLicenseKey(String licenseKey);
}
