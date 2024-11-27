package com.takameyer.modsink;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.takameyer.modsink.model.Member;
import com.takameyer.modsink.service.MemberRegistration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.logging.Logger;

@SpringBootTest
@ActiveProfiles("test") // Ensure test-specific configurations are loaded
public class MemberRegistrationIT {

    @Autowired
    private MemberRegistration memberRegistration;

    private static final Logger log = Logger.getLogger(MemberRegistrationIT.class.getName());

    @Test
    public void testRegister() throws Exception {
        // Create a new Member
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        // Perform the registration
        memberRegistration.register(newMember);

        // Assert that the Member ID was generated
        assertNotNull(newMember.getId(), "The member ID should not be null after registration");

        // Log the outcome
        log.info(newMember.getName() + " was persisted with ID " + newMember.getId());
    }
}
