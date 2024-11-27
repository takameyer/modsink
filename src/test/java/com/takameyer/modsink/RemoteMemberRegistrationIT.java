/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.takameyer.modsink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takameyer.modsink.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteMemberRegistrationIT {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SERVER_HOST = System.getenv("SERVER_HOST") != null
            ? System.getenv("SERVER_HOST")
            : System.getProperty("server.host", "http://localhost:8080/kitchensink");

    private String getHTTPEndpoint() {
        return SERVER_HOST + "/rest/members";
    }

    @Test
    public void testRegister() throws Exception {
        // Create a new Member
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        // Serialize the Member object to JSON
        String json = objectMapper.writeValueAsString(newMember);

        // Set up the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with the headers and JSON body
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        // Send the POST request
        ResponseEntity<String> response = restTemplate.postForEntity(getHTTPEndpoint(), requestEntity, String.class);

        // Assert the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isEmpty(), "Response body should be empty");
    }
}
