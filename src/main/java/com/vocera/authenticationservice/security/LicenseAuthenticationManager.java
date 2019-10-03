/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.security;

import com.vocera.authenticationservice.config.ClientConfig;
import com.vocera.authenticationservice.entity.FederatedLicense;
import com.vocera.authenticationservice.service.FederatedLicenseService;
import com.vocera.authenticationservice.service.LicenseKeyValidatorService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Authentication for Managing Licenses.
 *
 * @author Rohit Phatak
 */
@Component
public class LicenseAuthenticationManager implements AuthenticationManager {

    private static final Logger LOGGER = Logger.getLogger(LicenseAuthenticationManager.class.getName());

    private LicenseKeyValidatorService licenseKeyValidatorService;

    private FederatedLicenseService federatedLicenseService;

    private ClientConfig clientConfig;

    /**
     * Constructor for autowiring.
     *
     * @param licenseKeyValidatorService
     * @param clientConfig
     * @param federatedLicenseService
     */
    public LicenseAuthenticationManager(LicenseKeyValidatorService licenseKeyValidatorService,
                                        ClientConfig clientConfig, FederatedLicenseService federatedLicenseService) {
        this.licenseKeyValidatorService = licenseKeyValidatorService;
        this.clientConfig = clientConfig;
        this.federatedLicenseService = federatedLicenseService;
    }

    /**
     * Authenticator.
     *
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        if (!this.licenseKeyValidatorService.checkLicenseKey(username)) {
            throw new BadCredentialsException("Invalid License Key!!!");
        }

        FederatedLicense principal = this.federatedLicenseService.federateLicense(username);
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(clientConfig.getAuthority()));
        return new UsernamePasswordAuthenticationToken(principal.getId(), principal.getId(), grantedAuths);
    }

}
