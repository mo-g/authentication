/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Entity for persisting of license-key to organization mapping.
 *
 * @author Rohit Phatak
 */
@Entity
@SequenceGenerator(name = "license_gen", sequenceName = "license_sequence", allocationSize = 1)
public class FederatedLicense {

    /**
     * Primary key for LicenseStore.
     * When generated, this id is used as organization id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "license_gen")
    private long id;

    @Column(name = "license_key", unique = true)
    private String licenseKey;

    public FederatedLicense(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public FederatedLicense() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }
}
