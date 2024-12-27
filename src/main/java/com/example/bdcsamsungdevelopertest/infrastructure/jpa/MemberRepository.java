package com.example.bdcsamsungdevelopertest.infrastructure.jpa;

import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByNameAndEmailAndAddress(String name, String email, String address);
}