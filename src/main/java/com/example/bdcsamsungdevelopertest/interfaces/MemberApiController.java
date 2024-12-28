package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.MemberFacade;
import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.MemberInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO controller dto mismatch bad request handler needed
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
     * 400 bad, response: "유효성 검사 실패, 중복 이메일 등, 주소가 비어있는 경우, 주소가 너무 큰 경우)
     * */
    @PostMapping("/register")
    public ResponseEntity<MemberInfo.MemberEntity> registerMember(
        @RequestBody MemberDto.Register body
    ) {
        MemberCommand.Register command = new MemberCommand.Register(
            body.name(),
            body.email(),
            body.address()
        );
        MemberInfo.MemberEntity info = memberFacade.requestMemberRegistration(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(info);
    }

    /**
     * 특정 사용자 정보 조회
     * ※ QueryParameter 정의가 없으며, 특정 유저 정보 (unique or 1) 을 반환해야 되기 때문에 유저 값 전체를 받는 것으로... ※
     *
     * 200 ok, response: name, email, address
     * 404 not found, response:
     * */
    @PostMapping("/find")
    public ResponseEntity<MemberInfo.MemberEntity> searchMember(
        @RequestBody MemberDto.Search body
    ) {
        MemberRequestCommand command = new MemberRequestCommand(
            body.name(),
            body.email(),
            body.address()
        );
        MemberInfo.MemberEntity info = memberFacade.requestMemberSearch(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }
}
