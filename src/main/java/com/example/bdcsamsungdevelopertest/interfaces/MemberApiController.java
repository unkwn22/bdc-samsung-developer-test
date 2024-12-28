package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.MemberFacade;
import com.example.bdcsamsungdevelopertest.common.util.PageableExtension;
import com.example.bdcsamsungdevelopertest.domain.command.MemberCommand;
import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.MemberInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.MemberRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        @RequestBody MemberRequestDto body
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
        @RequestBody MemberRequestDto body
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

    /**
     * 전체 사용자 조회
     * ※ pagination 처리를 위한 queryParameter 상세 설명이 없기에 임의로 정의 ※
     * 응답 예제에 totalCount이 없기에 리스트만 반환
     * 응답 예제에 sortDirection 디폴트가 없기에 desc 디폴트
     * 빈 리스트일시 404 예외
     * size가 0보다 작을때 예외 명시가 없어서 400으로 임의
     *
     * 200 OK, response: [{name, email, address},...]
     * 404 not found, response:
     * */
    @GetMapping("/search")
    public ResponseEntity<List<MemberInfo.MemberEntity>> searchMembers(
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
        @RequestParam(value = "reqSortDir", required = false, defaultValue = "desc") String reqSortDir
    ) {
        MemberCommand.SearchList command = new MemberCommand.SearchList(
            PageableExtension.toPageable(page, size, reqSortDir)
        );
        List<MemberInfo.MemberEntity> infos = memberFacade.requestSearchMembers(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(infos);
    }

    /**
    * 특정 사용자 정보 수정
     * ※ 특정 사용자 정보 수정을 위한 유저 객체에 고유 키, uuid가 없음으로 (명세 없음) 또한
     * queryParameter 또는 pathVariable 명세가 없기 RequestBody에 고유키 검색을 email로 선정
     *
     * ※ 현 API 조건에 404 notfound가 존재, 유효한 유저 데이터를 먼저 조회 후 수정한 다음 결과 반환 해주기로...
     * Patch수정은 아닌듯해 보여서 Skip
     *
     * 200 OK, response: 수정 된 {name, email, address}
     * 400 bad request
     * 404 not found, response:
    * */
    @PutMapping("/update")
    public ResponseEntity<MemberInfo.MemberEntity> updateMember(
        @RequestBody MemberRequestDto body
    ) {
        MemberRequestCommand command = new MemberRequestCommand(
            body.name(),
            body.email(),
            body.address()
        );
        MemberInfo.MemberEntity info = memberFacade.requestMemberUpdate(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 사용자 삭제
     * ※ 특정 사용자 정보 수정을 위한 유저 객체에 고유 키, uuid가 없음으로 (명세 없음) 또한
     * queryParameter 또는 pathVariable 명세가 없기 RequestBody에 전체 사용자 정보로 조회 후 삭제
     *
     * 204 no content, response:
     * 404 not found, response:
     * */
    @DeleteMapping("/unregister")
    public ResponseEntity<Void> deleteMember(
        @RequestBody MemberRequestDto body
    ) {
        MemberRequestCommand command = new MemberRequestCommand(
            body.name(),
            body.email(),
            body.address()
        );
        memberFacade.requestMemberUnRegistration(command);
        return ResponseEntity.noContent().build();
    }
}
