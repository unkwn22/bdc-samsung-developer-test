package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Member;

import java.util.Optional;

public interface MemberReadWrite {

    /**
    * READ
    * */
    boolean validateIfEmailExists(String email);

    Optional<Member> findSpecificMember(String name, String email, String address);

    /**
    * WRITE
    * */
    Member saveMember(Member member);
}