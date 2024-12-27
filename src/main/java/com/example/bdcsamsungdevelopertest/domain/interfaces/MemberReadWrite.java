package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Member;

public interface MemberReadWrite {

    /**
    * READ
    * */
    boolean validateIfEmailExists(String email);

    /**
    * WRITE
    * */
    Member saveMember(Member member);
}