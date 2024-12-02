package com.takameyer.modsink.service;

import com.takameyer.modsink.data.MemberRepository;
import com.takameyer.modsink.exception.DuplicateEmailException;
import com.takameyer.modsink.model.Member;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Register a new member.
     * @param member - the member to register
     * @throws DuplicateEmailException - if the email already exists
     */
    public void register(Member member) {
        log.info("Registering " + member.getName());
        if (emailExists(member.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + member.getEmail());
        }
        memberRepository.save(member);
    }

    /**
     * Find a member by ID.
     * @param id - the ID to search for
     * @return member
     */
    public Member findById(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    /**
     * Find all members.
     * @return a list of all members
     */
    public List<Member> findAll() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    /**
     * Check if an email exists.
     * @param email - the email to check
     * @return true if the email exists, false otherwise
     */
    private boolean emailExists(String email) {
        return memberRepository.findByEmail(email) != null;
    }
}
