package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.DiscountFacade;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.DiscountRequestDto;
import com.example.bdcsamsungdevelopertest.interfaces.dto.MemberRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount/api/v1")
@Tag(name = "할인 관련 API", description = "할인 관련 API")
public class DiscountApiController {

    private final DiscountFacade discountFacade;

    public DiscountApiController(
        DiscountFacade discountFacade
    ) {
        this.discountFacade = discountFacade;
    }

    /**
     * 새로운 할인 생성
     *
     * 201 created: response: 생성된 상품 정보
     * 400 bad (유효성 검사 관련), response: 상품 정보 없음, 중복 할인
     * */
    @Operation(summary = "할인 생성", description = "새로운 할인을 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscountRequestDto.DiscountCreate.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<DiscountInfo.DiscountEntity> registerDiscount(
        @PathVariable(value = "hi") String hi,
        @RequestBody DiscountRequestDto.DiscountCreate body
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand(
            body.productId(),
            body.discountValue()
        );
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountRegistration(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(info);
    }

    /**
     * 특정 할인 정보 조회
     * ※ [GET] PathVariable {discountId} Long 타입과 겹쳐서 하위 url에 추가
     *
     * PathVariable {productId} (Long)
     * 200 ok: response: { "productId": 1, "discountValue": 1000 }
     * 404 notfound, response:
     * */
    @Operation(summary = "할인 조회", description = "상품 Id로 특정 할인 정보를 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping(value ="/byProduct/{productId}")
    public ResponseEntity<DiscountInfo.DiscountEntity> searchDiscountByProduct(
        @Parameter(description = "상품 id")
        @PathVariable("productId") Long productId,
        @RequestBody DiscountRequestDto body
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand();
        command.setProductId(productId);
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountSearchByProduct(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 할인 정보 조회
     * ※ [GET] PathVariable {productId} Long 타입과 겹쳐서 하위 url에 추가
     *
     * PathVariable {discountId} (Long)
     * 200 ok: response: { "productId": 1, "discountValue": 1000 }
     * 404 notfound, response:
     * */
    @Operation(summary = "할인 조회", description = "할인 id로 정보를 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping(value ="/byDiscount/{discountId}")
    public ResponseEntity<DiscountInfo.DiscountEntity> searchDiscount(
        @Parameter(description = "할인 id")
        @PathVariable("discountId") Long discountId
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand();
        command.setId(discountId);
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountSearch(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 할인 정보 수정
     *
     * PathVariable {discountId} (Long)
     * 200 ok, response: { "productId": 1, "discountValue": 1000 }
     * 400 bad, response:
     * 404 notfound, response:
     * */
    @Operation(summary = "할인 수정", description = "특정 할인 정보를 수정합니다")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = DiscountRequestDto.DiscountUpdate.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping(value ="/{discountId}")
    public ResponseEntity<DiscountInfo.DiscountEntity> updateDiscount(
        @Parameter(description = "할인 id")
        @PathVariable("discountId") Long discountId,
        @RequestBody DiscountRequestDto.DiscountUpdate body
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand();
        command.setDiscountValue(body.discountValue());
        command.setId(discountId);
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountUpdate(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 할인 정보 삭제
     *
     * 204 no content, response:
     * 404 notfound, response:
     * */
    @Operation(summary = "할인 삭제", description = "특정 할인을 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping(value = "/{discountId}")
    public ResponseEntity<Void> deleteDiscount(
        @Parameter(description = "할인 id")
        @PathVariable("discountId") Long id
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand();
        command.setId(id);
        discountFacade.requestDiscountUnRegistration(command);
        return ResponseEntity.noContent().build();
    }
}
