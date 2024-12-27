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

@Service
public class MemberService {

    private MemberReadWrite memberReadWrite;

    public MemberService(
        MemberReadWrite memberReadWrite
    ) {
        this.memberReadWrite = memberReadWrite;
    }

    @Transactional
    public MemberCommand.MemberEntityCommand createMember(
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
     * 2. subString한 이메일 아이디 중복 쿼리 검색
    * */
    @Transactional(readOnly = true)
    public String validateAndReturnParsedEmail(String email) {
        boolean patternValidation = Pattern.matches(EmailStaticValue.EMAIL_PATTERN_REGEX, email);
        if(!patternValidation) throw new BadRequestException("이메일 형식이 맞지 않습니다.");
        String parsedEmailId = ParseExtension.subStringEmail(email);
        boolean emailExists = memberReadWrite.validateIfEmailExists(parsedEmailId);
        if(emailExists) throw new BadRequestException("이미 존재하는 이메일 입니다.");
        return parsedEmailId;
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
    * CONSTRUCTOR
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

    public MemberCommand.MemberEntityCommand constructMemberEntityCommand(
        Member member
    ) {
        return new MemberCommand.MemberEntityCommand(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getAddress()
        );
    }

    public MemberInfo.MemberEntity constructMemberInfo(
        MemberCommand.MemberEntityCommand memberEntityCommand
    ) {
        return new MemberInfo.MemberEntity(
            memberEntityCommand.name(),
            memberEntityCommand.email(),
            memberEntityCommand.address()
        );
    }
}