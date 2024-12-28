package com.example.bdcsamsungdevelopertest.infrastructure.querydsl;

import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberQueryRepository {

    List<Member> findMembers(Pageable pageable);

    void updateMember(MemberRequestCommand updateCommand);
}
