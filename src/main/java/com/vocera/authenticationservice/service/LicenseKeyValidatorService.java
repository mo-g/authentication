/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.service;

import com.vocera.authenticationservice.config.ClientConfig;
import com.vocera.authenticationservice.entity.FederatedLicense;
import com.vocera.authenticationservice.exception.FederatedLicenseException;
import com.vocera.authenticationservice.security.LicenseUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Service for Validating License Key from the License Key Store.
 *
 * @author Rohit Phatak
 */
@Service
public class LicenseKeyValidatorService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(LicenseKeyValidatorService.class.getName());

    private ClientConfig clientConfig;

    private FederatedLicenseService federatedLicenseService;

    @Value("${license-store.file-path}")
    private String filePath;

    @Value("${license-store.file-name}")
    private String fileName;

    /**
     * Constructor for autowiring.
     *
     * @param clientConfig
     * @param federatedLicenseService
     */
    public LicenseKeyValidatorService(ClientConfig clientConfig, FederatedLicenseService federatedLicenseService) {
        this.clientConfig = clientConfig;
        this.federatedLicenseService = federatedLicenseService;
    }

    /**
     * @param licensekey
     * @return
     */
    public boolean checkLicenseKey(String licensekey) {
        return this.checkForKey(licensekey);
    }

    /**
     * @param organizationId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String organizationId) throws UsernameNotFoundException {
        try {
            FederatedLicense federation = this.federatedLicenseService.findByFederationId(Long.valueOf(organizationId));
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority(clientConfig.getAuthority()));
            return new LicenseUser(organizationId, organizationId, grantedAuths);
        } catch (FederatedLicenseException e) {
            throw new UsernameNotFoundException("Invalid Username : " + organizationId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method checks for license key into license key store.
     *
     * @param licenseKey
     * @return
     */
    private boolean checkForKey(String licenseKey) {
        LOGGER.info("License Store Path : " + filePath + "/" + fileName);
        try (Scanner scanner = new Scanner(new File(filePath, fileName))) {
            while (scanner.hasNext()) {
                if (licenseKey.equals(scanner.nextLine())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.severe("License Store not found on Classpath");
        }
        return false;
    }
}
