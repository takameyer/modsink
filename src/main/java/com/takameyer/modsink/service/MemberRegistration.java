package com.takameyer.modsink.service;

import com.takameyer.modsink.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.logging.Logger;

@Service
public class MemberRegistration {

    private final Logger log = Logger.getLogger(MemberRegistration.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void register(Member member) {
        log.info("Registering " + member.getName());
        em.persist(member);
        // Additional event handling can be implemented here if needed
    }
}
