package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.MemberInfo;
import com.example.bdcsamsungdevelopertest.domain.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberFacade {

    private final MemberService memberService;

    public MemberFacade(
        MemberService memberService
    ) {
        this.memberService = memberService;
    }

    /**
     * 사용자 생성 퍼사드
     *
     * DESC: 유저 생성을 위한 요청 집합 메소드
     *
     * ORDER:
     * 1. 이메일 유효성 검사
     * 2. 주소 유효성 검사
     * 3. 새로운 command로 유저 객체 캡슐화
     * 4. 유저 생성
     * 5. 유저 데이터 캡슐화
    * */
    public MemberInfo.MemberEntity requestMemberRegistration(
        MemberCommand.Register registerCommand
    ) {
        String validatedParsedEmail
                = memberService.validateEmailForRegistrationAndReturnParsedEmail(registerCommand.email());
        memberService.validateAddressLength(registerCommand.address());
        MemberCommand.ValidatedRegister validatedRegisterCommand
                = memberService.toValidatedRegisterCommand(registerCommand, validatedParsedEmail);
        MemberCommand.MemberEntity memberEntityCommand
                = memberService.createMember(validatedRegisterCommand);
        return memberService.toMemberInfo(memberEntityCommand);
    }

    /**
    * 사용자 조회 퍼사드
     *
     * DESC: 특정 유저를 조회 하기 위한 집합 메소드
     *
     * ORDER:
     * 1. 이메일 형식 확인
     * 2. 유저 조회 커맨드 set
     * 3. 유저 조회
     * 4. 반환 객체 캡슐화
    * */
    public MemberInfo.MemberEntity requestMemberSearch(
        MemberRequestCommand searchCommand
    ) {
        String parsedEmail = memberService.validateEmailPatternAndReturnParsedEmail(searchCommand.getEmail());
        searchCommand.setEmail(parsedEmail);
        MemberCommand.MemberEntity memberEntityCommand = memberService.searchMember(searchCommand);
        return memberService.toMemberInfo(memberEntityCommand);
    }
}