/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice.config;

import com.vocera.authenticationservice.service.KeyStoreService;
import com.vocera.authenticationservice.service.LicenseKeyValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
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

    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private LicenseKeyValidatorService licenseUserService;

    private ClientConfig clientConfig;

    private KeyStoreService keyStoreService;

    private TokenEnhancer tokenEnhancer;

    public AuthServerConfig(BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                            LicenseKeyValidatorService licenseUserService, ClientConfig clientConfig,
                            KeyStoreService keyStoreService, TokenEnhancer tokenEnhancer) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.licenseUserService = licenseUserService;
        this.clientConfig = clientConfig;
        this.keyStoreService = keyStoreService;
        this.tokenEnhancer = tokenEnhancer;
    }

    /**
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
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        LOGGER.info("Scopes : " + clientConfig.getScopes());
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
     * Authorization Server Endpoints Configurer which uses License Based User Service and provides an Authentication
     * Manager
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(
            AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.tokenEnhancer, accessTokenConverter()));

        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(licenseUserService)
                .authenticationManager(authenticationManager);
    }

    /**
     * Creating bean for Jwt tokenstore.
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Creating bean for accesstokenconverter.
     * Uses {@link KeyStoreService} for obtaining a key pair.
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(this.keyStoreService.getKeyPair());
        return converter;
    }

    /**
     * Bean for tokenservices.
     *
     * @return
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

}
