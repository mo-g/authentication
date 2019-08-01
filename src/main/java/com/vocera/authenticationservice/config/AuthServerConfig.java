/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.config;

import com.vocera.authenticationservice.service.LicenseKeyValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import java.util.logging.Logger;

/**
 * Security Configuration for Authorization Server.
 *
 * @author Rohit Phatak
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final Logger LOGGER = Logger.getLogger(AuthServerConfig.class.getName());

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LicenseKeyValidatorService licenseUserService;

    @Autowired
    private ClientConfig clientConfig;

    /**
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }

    /**
     * Configuration for Clients.
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        LOGGER.info("Scopes : "+clientConfig.getScopes());
        clients
                .inMemory()
                .withClient(clientConfig.getName()).secret(passwordEncoder.encode(clientConfig.getSecret()))
                .authorizedGrantTypes(clientConfig.getGrantTypes())
                .authorities(clientConfig.getAuthority())
                .scopes(clientConfig.getScopes())
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240000);
    }

    /**
     * Authorization Server Endpoints Configurer which uses License Based User Service and provides an Authentication Manager
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(
            AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {

        endpoints
                .userDetailsService(licenseUserService)
                .authenticationManager(authenticationManager);
    }

}
