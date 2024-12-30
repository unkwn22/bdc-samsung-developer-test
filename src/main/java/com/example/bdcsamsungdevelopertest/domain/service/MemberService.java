package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.exception.NotFoundException;
import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import com.example.bdcsamsungdevelopertest.domain.interfaces.MemberReadWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.bdcsamsungdevelopertest.common.util.EmailStaticValue.EMAIL_PATTERN_REGEX;
import static com.example.bdcsamsungdevelopertest.common.util.StringUtilExtension.subStringEmail;
import static com.example.bdcsamsungdevelopertest.domain.command.ToConversion.toMemberEntitiesCommand;
import static com.example.bdcsamsungdevelopertest.domain.command.ToConversion.toMemberEntityCommand;

@Service
public class MemberService {

    private final MemberReadWrite memberReadWrite;

    public MemberService(
        MemberReadWrite memberReadWrite
    ) {
        this.memberReadWrite = memberReadWrite;
    }

    @Transactional
    public MemberCommand.MemberEntity createMember(
        MemberRequestCommand registerCommand
    ) {
        Member beforeSaveMemberEntity = new Member(registerCommand);
        Member savedMember = memberReadWrite.saveMember(beforeSaveMemberEntity);
        return toMemberEntityCommand(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberCommand.MemberEntity searchMember(
        MemberRequestCommand searchCommand
    ) {
        Member member = fullMemberSearch(searchCommand);
        return toMemberEntityCommand(member);
    }

    @Transactional(readOnly = true)
    public boolean searchByParsedEmail(String parsedEmail) {
        return memberReadWrite.validateIfEmailExists(parsedEmail);
    }

    @Transactional(readOnly = true)
    public List<MemberCommand.MemberEntity> searchMembers(
        MemberCommand.SearchList searchListCommand
    ) {
        List<Member> searchedMembers = memberReadWrite.customFindMembers(searchListCommand.pageable());
        if(searchedMembers.isEmpty()) throw new NotFoundException("유저 기록이 없습니다.");
        return toMemberEntitiesCommand(searchedMembers);
    }

    @Transactional
    public void findMemberAndUpdate(
        MemberRequestCommand updateCommand
    ) {
        Optional<Member> searchedMemberObject = memberReadWrite.findSpecificMemberByEmail(updateCommand.getEmail());
        Member member = memberGetOrThrow(searchedMemberObject);
        member.updateMember(updateCommand);
    }

    private Member fullMemberSearch(
        MemberRequestCommand commonCommand
    ) {
        Optional<Member> searchedMemberObject = memberReadWrite.findSpecificMember(
            commonCommand.getName(),
            commonCommand.getEmail(),
            commonCommand.getAddress()
        );
        return memberGetOrThrow(searchedMemberObject);
    }

    @Transactional
    public void findMemberAndDelete(
        MemberRequestCommand deleteCommand
    ) {
        Member member = fullMemberSearch(deleteCommand);
        memberReadWrite.deleteMember(member);
    }

    @Transactional(readOnly = true)
    public MemberCommand.MemberEntity searchMember(Long id) {
        Optional<Member> searchedMemberObject = memberReadWrite.findSpecificMember(id);
        Member member = memberGetOrThrow(searchedMemberObject);
        return toMemberEntityCommand(member);
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
     * @samsung 이메일 형식 유효성 검사
     * */
    public String validateEmailPatternAndReturnParsedEmail(String email) {
        boolean patternMatch = Pattern.matches(EMAIL_PATTERN_REGEX, email);
        if(!patternMatch) throw new BadRequestException("이메일 형식이 맞지 않습니다.");
        return parseEmail(email);
    }

    /**
    * 이메일 중복 확인
    * */
    private void validateDuplicateEmail(boolean emailDupValidation) {
        if(emailDupValidation) throw new BadRequestException("이미 존재하는 이메일 입니다.");
    }

    /**
    * Optional member 객체 유효검사
    * */
    public Member memberGetOrThrow(Optional<Member> searchedMemberObject) {
        if(searchedMemberObject.isEmpty()) throw new NotFoundException("존재하지 않는 유저 정보입니다.");
        return searchedMemberObject.get();
    }

    /**
    * CONSTRUCTOR & METHODS
    * */

    /**
     * 이메일 파싱
     * */
    private String parseEmail(String email) {
        return subStringEmail(email);
    }
}