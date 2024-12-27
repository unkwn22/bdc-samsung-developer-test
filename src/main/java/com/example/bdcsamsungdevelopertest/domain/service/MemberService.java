package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.util.EmailStaticValue;
import com.example.bdcsamsungdevelopertest.common.util.ParseExtension;
import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import com.example.bdcsamsungdevelopertest.domain.info.MemberInfo;
import com.example.bdcsamsungdevelopertest.domain.interfaces.MemberReadWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

import static com.example.bdcsamsungdevelopertest.common.util.EmailStaticValue.EMAIL_PATTERN_REGEX;
import static com.example.bdcsamsungdevelopertest.common.util.EmailStaticValue.SAMSUNG_EMAIL;

@Service
public class MemberService {

    private MemberReadWrite memberReadWrite;

    public MemberService(
        MemberReadWrite memberReadWrite
    ) {
        this.memberReadWrite = memberReadWrite;
    }

    @Transactional
    public MemberCommand.MemberEntity createMember(
        MemberCommand.ValidatedRegister validatedRegisterCommand
    ) {
        Member beforeSaveMemberEntity = new Member(validatedRegisterCommand);
        Member savedMember = memberReadWrite.saveMember(beforeSaveMemberEntity);
        return constructMemberEntityCommand(savedMember);
    }

    /**
    * VALIDATION
    * */

    /**
     * 이메일 유효성 검사
     *
     * DESC: 이메일 아이디 중복검색 최적화를 위한 메소드
     * LIKE 검색의 성능을 지양하고 이메일 아이디를 서브스트링하여
     * DB에서 '='로 쿼리조회
     *
     * ORDER:
     * 1. @samsung.com 패턴 regex 검사
     * 2. email subString
     * 3. subString한 이메일 아이디 중복 쿼리 검색
     * 4. 중복 유효검사
     * 5. subString email 반환
    * */
    public String validateEmailForRegistrationAndReturnParsedEmail(String email) {
        String parsedEmail = validateEmailPatternAndReturnParsedEmail(email);
        boolean emailExists = searchByParsedEmail(parsedEmail);
        validateDuplicateEmail(emailExists);
        return parsedEmail;
    }

    /**
    * 주소 길이 유효성 검사
    * */
    public void validateAddressLength(String address) {
        int addressLength = address.length();
        boolean validatedLength = addressLength > 0 && addressLength < 101;
        if(!validatedLength) throw new BadRequestException("주소 길이가 너무 큽니다.");
    }

    /**
    * CONSTRUCTOR & METHODS
    * */
    public MemberCommand.ValidatedRegister constructValidatedRegisterCommand(
        MemberCommand.Register registerCommand,
        String parsedEmail
    ) {
        return new MemberCommand.ValidatedRegister(
            registerCommand.name(),
            parsedEmail,
            registerCommand.address()
        );
    }

    public MemberCommand.MemberEntity constructMemberEntityCommand(
        Member member
    ) {
        return new MemberCommand.MemberEntity(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getAddress()
        );
    }

    public MemberInfo.MemberEntity constructMemberInfo(
        MemberCommand.MemberEntity memberEntityCommand
    ) {
        return new MemberInfo.MemberEntity(
            memberEntityCommand.name(),
            memberEntityCommand.email() + SAMSUNG_EMAIL,
            memberEntityCommand.address()
        );
    }

    /**
     * 이메일 파싱
     * */
    private String parseEmail(String email) {
        return ParseExtension.subStringEmail(email);
    }
}