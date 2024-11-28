package com.takameyer.modsink.service;

import com.takameyer.modsink.data.MemberRepository;
import com.takameyer.modsink.exception.DuplicateEmailException;
import com.takameyer.modsink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class MemberService {

    private final Logger log = Logger.getLogger(MemberService.class.getName());

    @Autowired
    private MemberRepository memberRepository;

    public void register(Member member) {
        log.info("Registering " + member.getName());
        if (emailExists(member.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + member.getEmail());
        }
        memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    private boolean emailExists(String email) {
        return memberRepository.findByEmail(email) != null;
    }
}
