package com.takameyer.modsink.data;

import com.takameyer.modsink.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // findById is already defined in JpaRepository

    Member findByEmail(String email);

    Member findByName(String name);

    Member findByPhoneNumber(String phoneNumber);

    @Query("SELECT m FROM Member m ORDER BY m.name ASC")
    List<Member> findAllByOrderByName();
}
