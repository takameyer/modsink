package com.takameyer.modsink;

import com.takameyer.modsink.model.Member;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidMember() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    public void testInvalidName() {
        Member member = new Member();
        member.setName("John123");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "There should be one validation error for the name");
    }

    @Test
    public void testInvalidEmail() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("invalid-email");
        member.setPhoneNumber("1234567890");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "There should be one validation error for the email");
    }

    @Test
    public void testInvalidPhoneNumber() {
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("12345");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        assertEquals(1, violations.size(), "There should be one validation error for the phone number");
    }
}
