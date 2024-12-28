package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.MemberInfo;
import com.example.bdcsamsungdevelopertest.domain.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 3. 유효한 이메일 다시 command에 set
     * 4. 유저 생성
     * 5. 유저 데이터 캡슐화
    * */
    public MemberInfo.MemberEntity requestMemberRegistration(
        MemberRequestCommand registerCommand
    ) {
        String validatedParsedEmail = memberService.validateEmailForRegistrationAndReturnParsedEmail(registerCommand.getEmail());
        memberService.validateAddressLength(registerCommand.getAddress());
        registerCommand.setEmail(validatedParsedEmail);
        MemberCommand.MemberEntity memberEntityCommand = memberService.createMember(registerCommand);
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

    /**
    * 전체 사용자 조회 퍼사드
     *
     * DESC: pagination 요청으로 유저 리스트 반환
     *
     * ORDER:
     * 1. pageable 객체로 유저 리스트 조회
     * 2. info 리스트로 변환 후 반환
    * */
    public List<MemberInfo.MemberEntity> requestSearchMembers(
        MemberCommand.SearchList searchListCommand
    ) {
        List<MemberCommand.MemberEntity> memberEntitiesCommand = memberService.searchMembers(searchListCommand);
        return memberService.toMemberInfos(memberEntitiesCommand);
    }

    /**
    * 특정 사용자 수정 퍼사드
     *
     * DESC: 영속성 컨텍스트를 이용한 객체 update
     *
     * ORDER:
     * 1. 요청 이메일 형식 유효성 검사
     * 2. 요청 주소 유효성 검사
     * 3. 유효한 이메일 다시 command에 set
     * 4. 유저 조회 및 수정
     * 5. info 객체로 변환 후 반환
    * */
    public MemberInfo.MemberEntity requestMemberUpdate(
        MemberRequestCommand updateCommand
    ) {
        String parsedEmail = memberService.validateEmailPatternAndReturnParsedEmail(updateCommand.getEmail());
        memberService.validateAddressLength(updateCommand.getAddress());
        updateCommand.setEmail(parsedEmail);
        memberService.findMemberAndUpdate(updateCommand);
        MemberCommand.MemberEntity memberEntityCommand = memberService.searchMember(updateCommand);
        return memberService.toMemberInfo(memberEntityCommand);
    }

    /**
    * 특정 사용자 삭제 퍼사드
     *
     * ORDER:
     * 1. 요청 이메일 형식 유효성 검사
     * 2. 유효한 이메일 다시 command에 set
     * 3. 사용자 검색 후 삭제
    * */
    public void requestMemberUnRegistration(
        MemberRequestCommand deleteCommand
    ) {
        String parsedEmail = memberService.validateEmailPatternAndReturnParsedEmail(deleteCommand.getEmail());
        deleteCommand.setEmail(parsedEmail);
        memberService.findMemberAndDelete(deleteCommand);
    }
}