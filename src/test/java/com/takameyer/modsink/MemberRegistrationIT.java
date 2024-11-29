package com.takameyer.modsink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.takameyer.modsink.data.MemberRepository;
import com.takameyer.modsink.model.Member;
import com.takameyer.modsink.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest
@ActiveProfiles("test") // Ensure test-specific configurations are loaded
public class MemberRegistrationIT {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private static final Logger log = Logger.getLogger(MemberRegistrationIT.class.getName());

    @BeforeEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }


    @Test
    public void testRegister() throws Exception {
        // Create a new Member
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

        // Perform the registration
        memberService.register(newMember);

        // Assert that the Member ID was generated
        assertNotNull(newMember.getId(), "The member ID should not be null after registration");

        // Log the outcome
        log.info(newMember.getName() + " was persisted with ID " + newMember.getId());
    }

    @Test
    public void testFindMethods() throws Exception {
        // Create a bunch of members
        Member member1 = new Member();
        member1.setName("John Doe");
        member1.setEmail("john@mailinator.com");
        member1.setPhoneNumber("2125551234");

        Member member2 = new Member();
        member2.setName("Jane Doe");
        member2.setEmail("jane@mailinator.com");
        member2.setPhoneNumber("2125551236");

        Member member3 = new Member();
        member3.setName("Jim Doe");
        member3.setEmail("jim@mailinator.com");
        member3.setPhoneNumber("2125551237");

        // Perform the registration
        memberService.register(member1);
        memberService.register(member2);
        memberService.register(member3);

        // Find the Member by email
        Member foundMember = memberRepository.findByEmail("john@mailinator.com");
        assertNotNull(foundMember, "The member should not be null");

        String memberId = foundMember.getId();

        // Find member by id
        foundMember = memberRepository.findById(memberId).get();
        assertNotNull(foundMember, "The member should not be null");
        assertEquals("John Doe", foundMember.getName(), "The member should be John Doe");

        // Find the Member by name
        foundMember = memberRepository.findByName("Jane Doe");
        assertNotNull(foundMember, "The member should not be null");
        assertEquals("Jane Doe", foundMember.getName(), "The member should be Jane Doe");

        // Find the Member by phone number
        foundMember = memberRepository.findByPhoneNumber("2125551234");
        assertNotNull(foundMember, "The member should not be null");
        assertEquals("John Doe", foundMember.getName(), "The member should be John Doe");

        // Find all Members
        Iterable<Member> allMembers = memberRepository.findAll();
        assertNotNull(allMembers, "The members should not be null");
        assertEquals(3, ((ArrayList<Member>) allMembers).size(), "There should be 3 members");

        // Find all Members ordered by name
        List<Member> allMembersOrdered = memberRepository.findAllByOrderByNameAsc();
        assertNotNull(allMembersOrdered, "The members should not be null");

        assertEquals(3, ((ArrayList<Member>) allMembers).size(), "There should be 3 members");

        assertEquals("Jane Doe", allMembersOrdered.get(0).getName(), "The first member should be Jane Doe");
        assertEquals("Jim Doe", allMembersOrdered.get(1).getName(), "The second member should be Jim Doe");
        assertEquals("John Doe", allMembersOrdered.get(2).getName(), "The third member should be John Doe");


        // Log the outcome
        log.info("All members were found successfully");
    }

    @Test
    public void testDuplicateEmail() {
        // Create a new Member
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@email.com");
        newMember.setPhoneNumber("2125551234");

        // Perform the registration
        memberService.register(newMember);

        // Create another Member with the same email
        Member duplicateMember = new Member();
        duplicateMember.setName("Jane Doe");
        duplicateMember.setEmail("jane@email.com");
        duplicateMember.setPhoneNumber("2125551234");

        // Assert that the correct exception was thrown
        try {
            memberService.register(duplicateMember);
        } catch (Exception e) {
            log.info("Duplicate email was caught successfully");
            Assertions.assertEquals("Email already exists: " + duplicateMember.getEmail(), e.getMessage());
        }

    }
}
