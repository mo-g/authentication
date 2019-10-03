/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for License Key Validator Service.
 *
 * @author Rohit Phatak
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LicenseKeyValidatorServiceTest {

    @Autowired
    private LicenseKeyValidatorService licenseKeyValidatorService;

    /**
     * Success case for checking License key.
     */
    @Test
    public void checkValidLicenseKey() {
        System.out.println("Executing Test for Valid License Key");
        assertTrue(licenseKeyValidatorService.checkLicenseKey("1234"));
    }

    /**
     * Failure case for checking License key.
     */
    @Test
    public void checkInValidLicenseKey() {
        System.out.println("Executing Test for Invalid License Key");
        assertFalse(licenseKeyValidatorService.checkLicenseKey("12345"));
    }

    /**
     * Success case for loadByUsername().
     */
    @Test
    public void validLoadUserByUsername() {
        System.out.println("Executing Test for successful userdetails resolution.");
        UserDetails result = licenseKeyValidatorService.loadUserByUsername("1");
        assertNotNull(result);
    }

    /**
     * Failure case for loadByUsername().
     * Returns @{@link UsernameNotFoundException}
     */
    @Test
    public void invalidLoadUserByUsername() {
        System.out.println("Executing Test for invalid userdetails resolution.");
        assertThrows(UsernameNotFoundException.class, () -> {
            licenseKeyValidatorService.loadUserByUsername("12345");
        });
    }
}