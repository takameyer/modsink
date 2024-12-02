package com.takameyer.modsink.data;

import com.takameyer.modsink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberRepository extends MongoRepository<Member, String> {

    /**
     * Find a member by email.
     *
     * @param email the email to search for
     * @return the member with the given email, or null if not found
     */
    Member findByEmail(String email);

    /**
     * Find a member by name.
     *
     * @param name the name to search for
     * @return the member with the given name, or null if not found
     */
    Member findByName(String name);

    /**
     * Find a member by phone number.
     *
     * @param phoneNumber the phone number to search for
     * @return the member with the given phone number, or null if not found
     */
    Member findByPhoneNumber(String phoneNumber);

    /**
     * Find all members, ordered by name.
     *
     * @return a list of all members, ordered by name
     */
    List<Member> findAllByOrderByNameAsc();

}
