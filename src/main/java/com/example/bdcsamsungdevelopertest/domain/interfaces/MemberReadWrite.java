package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberReadWrite {

    /**
    * READ
    * */
    boolean validateIfEmailExists(String email);

    Optional<Member> findSpecificMember(String name, String email, String address);

    // TODO use if totalCount is needed
    Page<Member> findMembers(Pageable pageable);

    List<Member> customFindMembers(Pageable pageable);

    Optional<Member> findSpecificMemberByEmail(String email);

    /**
    * WRITE
    * */
    Member saveMember(Member member);

    // TODO use if command contains entity id
    void customUpdateMember(MemberRequestCommand updateCommand);

    void deleteMember(Member member);
}