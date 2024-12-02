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
import com.takameyer.modsink.data.MemberRepository;
import com.takameyer.modsink.model.Member;
import com.takameyer.modsink.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RemoteMemberRegistrationIT {

    @LocalServerPort
    private int port;

    // Start a MongoDB container
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:7.0");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeAll
    static void beforeAll() {
        mongodb.start();
    }

    @AfterAll
    static void afterAll() {
        mongodb.close();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongodb::getReplicaSetUrl);
    }

    // Ensure the tests don't collide with each other
    @BeforeEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    private String getHTTPEndpoint() {
        return "http://localhost:" + port + "/api/members";
    }

    @Test
    public void testRegister() throws Exception {
        //log the getHTTPEndpoint result
        System.out.println(getHTTPEndpoint());

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
        Member registeredMember = objectMapper.readValue(response.getBody(), Member.class);
        Assertions.assertNotNull(registeredMember.getId(), "The member ID should not be null after registration");
        Assertions.assertEquals(newMember.getName(), registeredMember.getName(), "The registered member name should match the input");
    }

    @Test
    public void testInvalidEmailFormat() throws Exception {
        // Create a new Member with an invalid email
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("invalid-email");
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
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("must be a well-formed email address"), "The error message should indicate an invalid email format");
    }

    @Test
    public void testInvalidPhoneNumber() throws Exception {
        // Create a new Member with an invalid phone number
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("12345");

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
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("size must be between 10 and 12"), "The error message should indicate an invalid phone number");
    }

    @Test
    public void testDuplicateEmail() throws Exception {
        // Create a new Member
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        // Register the member
        memberService.register(newMember);

        // Attempt to register another member with the same email
        Member duplicateMember = new Member();
        duplicateMember.setName("John Doe");
        duplicateMember.setEmail("jane@mailinator.com");
        duplicateMember.setPhoneNumber("2125555678");

        // Serialize the Member object to JSON
        String json = objectMapper.writeValueAsString(duplicateMember);

        // Set up the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with the headers and JSON body
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        // Send the POST request
        ResponseEntity<String> response = restTemplate.postForEntity(getHTTPEndpoint(), requestEntity, String.class);

        // Assert the response
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Email already exists"), "The error message should indicate a duplicate email");
    }

}
