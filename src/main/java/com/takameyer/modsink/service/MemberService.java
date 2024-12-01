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

    public void register(Member member) {
        log.info("Registering " + member.getName());
        if (emailExists(member.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + member.getEmail());
        }
        memberRepository.save(member);
    }

    public Member findById(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> findAll() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    private boolean emailExists(String email) {
        return memberRepository.findByEmail(email) != null;
    }
}
