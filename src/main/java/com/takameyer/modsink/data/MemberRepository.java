package com.takameyer.modsink.data;

import com.takameyer.modsink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberRepository extends MongoRepository<Member, String> {

    // findById is already defined in JpaRepository

    Member findByEmail(String email);

    Member findByName(String name);

    Member findByPhoneNumber(String phoneNumber);

    List<Member> findAllByOrderByNameAsc();

}
