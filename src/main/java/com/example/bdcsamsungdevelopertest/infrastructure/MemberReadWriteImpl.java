package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import com.example.bdcsamsungdevelopertest.domain.interfaces.MemberReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.MemberRepository;
import com.example.bdcsamsungdevelopertest.infrastructure.querydsl.MemberQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberReadWriteImpl implements MemberReadWrite {

    private MemberRepository memberRepository;              // JPA Repositoy
    private MemberQueryRepository memberQueryRepository;    // Querydsl Repsitory

    public MemberReadWriteImpl(
        MemberRepository memberRepository,
        MemberQueryRepository memberQueryRepository
    ) {
        this.memberRepository = memberRepository;
        this.memberQueryRepository = memberQueryRepository;
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
    public Page<Member> findMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public List<Member> customFindMembers(Pageable pageable) {
        return memberQueryRepository.findMembers(pageable);
    }

    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
}