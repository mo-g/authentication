/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.service.impl;

import com.vocera.authenticationservice.entity.FederatedLicense;
import com.vocera.authenticationservice.exception.FederatedLicenseException;
import com.vocera.authenticationservice.repository.FederatedLicenseRepository;
import com.vocera.authenticationservice.service.FederatedLicenseService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementation for {@link FederatedLicenseService}.
 *
 * @author Rohit Phatak
 */
@Service
public class FederatedLicenseServiceImpl implements FederatedLicenseService {

    private static final Logger LOGGER = Logger.getLogger(FederatedLicenseServiceImpl.class.getName());

    private FederatedLicenseRepository federatedLicenseRepository;

    /**
     * Constructor for autowiring.
     *
     * @param federatedLicenseRepository
     */
    public FederatedLicenseServiceImpl(FederatedLicenseRepository federatedLicenseRepository) {
        this.federatedLicenseRepository = federatedLicenseRepository;
    }

    /**
     * Check if the license is federated.
     *
     * @param licenseKey
     * @return
     */
    @Override
    public FederatedLicense checkLicense(String licenseKey) {
        LOGGER.info("Check license called on : " + licenseKey);
        Optional<FederatedLicense> federatedLicense = this.federatedLicenseRepository.findByLicenseKey(licenseKey);
        if (federatedLicense.isPresent()) {
            return federatedLicense.get();
        } else {
            throw new FederatedLicenseException("No Federation Present for the license !!!!");
        }
    }

    /**
     * Check if the license is federated.
     * If not federate the license and return it.
     *
     * @param licenseKey
     * @return
     */
    @Override
    public FederatedLicense federateLicense(String licenseKey) {
        LOGGER.info("Federate license called : " + licenseKey);
        try {
            return this.checkLicense(licenseKey);
        } catch (FederatedLicenseException e) {
            return this.federatedLicenseRepository.save(new FederatedLicense(licenseKey));
        } catch (Exception e) {
            throw new FederatedLicenseException("Could not Federate the license !!!!");
        }
    }

    /**
     * @param federationId
     * @return
     */
    @Override
    public FederatedLicense findByFederationId(Long federationId) {
        LOGGER.info("Find by federation id called : " + federationId);

        Optional<FederatedLicense> federatedLicense = this.federatedLicenseRepository.findById(federationId);
        if (federatedLicense.isPresent()) {
            return federatedLicense.get();
        } else {
            throw new FederatedLicenseException("Invalid FederationId");
        }
    }
}
