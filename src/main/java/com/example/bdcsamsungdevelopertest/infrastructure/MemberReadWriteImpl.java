package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import com.example.bdcsamsungdevelopertest.domain.interfaces.MemberReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberReadWriteImpl implements MemberReadWrite {

    private MemberRepository memberRepository;

    public MemberReadWriteImpl(
        MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean validateIfEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public Optional<Member> findSpecificMember(String name, String email, String address) {
        return memberRepository.findByNameAndEmailAndAddress(name, email, address);
    }

    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
}