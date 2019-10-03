/*
 * Copyright (c) Vocera Communications, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * Vocera Communications, Inc.
 */

package com.vocera.authenticationservice;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rohit Phatak
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthenticationServiceApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    /**
     * Test case for checking if access and refresh tokens are issued.
     *
     * @throws Exception
     */
    @Test
    public void checkAccessToken() throws Exception {
        System.out.println("Executing Test case for checking access and refresh tokens are issued.");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "1234");

        mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("api-gateway", "api-gateway"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").hasJsonPath());

    }

    /**
     * Check if a new access token is issued against refresh token.
     *
     * @throws Exception
     */
    @Test
    public void checkRefreshToken() throws Exception {
        System.out.println("Executing Test case for checking access and refresh tokens are issued.");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "1234");

        MvcResult response = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("api-gateway", "api-gateway"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").hasJsonPath())
                .andReturn();

        JSONObject tokenResponse = new JSONObject(response.getResponse().getContentAsString());
        params.set("grant_type", "refresh_token");
        params.add("refresh_token", tokenResponse.get("refresh_token").toString());
        params.remove("username");

        mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("api-gateway", "api-gateway"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").hasJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").hasJsonPath());
    }

}
