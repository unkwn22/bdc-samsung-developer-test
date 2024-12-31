package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.ProductFacade;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.ProductInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.DiscountRequestDto;
import com.example.bdcsamsungdevelopertest.interfaces.dto.ProductRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/api/v1")
@Tag(name = "상품 관련 API", description = "상품 관련 API")
public class ProductApiController {

    private final ProductFacade productFacade;

    public ProductApiController(
        ProductFacade productFacade
    ) {
        this.productFacade = productFacade;
    }

    /**
     * 새로운 상품 생성
     *
     * 201 created: response: 생성된 상품 정보
     * 400 bad (유효성 검사 관련), response:
    * */
    @Operation(summary = "상품 생성", description = "새로운 상품을 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductRequestDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<ProductInfo> registerProduct(
        @RequestBody ProductRequestDto body
    ) {
        ProductRequestCommand command = new ProductRequestCommand(
            body.name(),
            body.price()
        );
        ProductInfo info = productFacade.requestProductRegistration(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(info);
    }

    /**
     * 특정 상품 정보 조회
     *
     * PathVariable {productId} (Long)
     * 200 ok: response: 해당 상품 정보 (discount 포함)
     * 404 notfound, response:
     * */
    @Operation(summary = "상품 조회", description = "특정 상품 정보를 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping(value = "/{productId}")
    public ResponseEntity<ProductInfo> searchProduct(
        @Parameter(description = "상품 id")
        @PathVariable("productId") Long id
    ) {
        ProductRequestCommand command = new ProductRequestCommand(id);
        ProductInfo info = productFacade.requestProductSearch(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 상품 정보 수정
     *
     * ※ Patch 수정인지, 전체 수정인지 불명확
     * 설명이 부족함으로, client에서 변경된 상품 정보를 토대로 수정할 전체 정보를 보내주는 것으로 임의
     * 단, 상품명 유니크 유효성 검사는 수행
     *
     * "상품명이 변경되었을때 상품명은 여전히 유일한 상품명을 갖고 있어야합니다." 요청은 Patch인데 API 기준명세서 body 템플릿은
     * 전체를 보내는 것으로 보임?, nullable 명시 없음?... 그럼으로 전체를 보내는다는 것으로 이해
     * 받은 전체 body 정보가 조회 대상 productId의 상품명이 변동 될 것인지와 고유성 확인은 자체적으로 필요
     *
     * PathVariable {productId} (Long)
     * RequestBody: { "name": "스마트폰", "price": 500 }
     *
     * 200 ok, response: 해당 상품 정보 (discount 포함)
     * 400 bad, response: 유니크 하지 않는 상품명, 수정 가격이 할인 금액보다 작을 때
     * 404 not found, response: 없는 상품 대상으로 검색시
     * */
    @Operation(summary = "상품 수정", description = "특정 상품 정보를 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ok",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductRequestDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PutMapping(value = "/{productId}")
    public ResponseEntity<ProductInfo> updateProduct(
        @Parameter(description = "상품 id")
        @PathVariable("productId") Long id,
        @RequestBody ProductRequestDto body
    ) {
        ProductRequestCommand command = new ProductRequestCommand(
            id,
            body.name(),
            body.price()
        );
        ProductInfo info = productFacade.requestProductUpdate(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 모든 상품 조회
     *
     * ※ pageable 명세가 없음
     * 404 명세가 없음으로 조회할 목록이 없더라도 빈 리스트 반환으로 인지
     *
     * 200 ok: response: [ {상품 정보, {해당 상품 정보 (discount 포함)}} ]
     * */
    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록을 조회합니다")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductInfo>> searchProducts() {
        List<ProductInfo> infos = productFacade.requestSearchProducts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(infos);
    }
}
