package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.MemberFacade;
import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.interfaces.dto.MemberDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/api/v1")
public class MemberApiController {

    private final MemberFacade memberFacade;

    public MemberApiController(
        MemberFacade memberFacade
    ) {
        this.memberFacade = memberFacade;
    }

    /**
     * 새로운 사용자를 생성
     * 201 created, response: 생성된 사용자 정보 (JSON)
     * 404 bad, response: "유효성 검사 실패, 중복 이메일 등, 주소가 비어있는 경우, 주소가 너무 큰 경우)
     * */
    @PostMapping("/register")
    //TODO return type
    public void registerMember(
        @RequestBody MemberDto.Register body
    ) {
        MemberCommand.Register command = new MemberCommand.Register(
            body.name(),
            body.email(),
            body.address()
        );
        memberFacade.requestMemberRegistration(command);
    }
}
