package com.godchigam.godchigam.domain.groupbuying.controller;

import com.godchigam.godchigam.domain.groupbuying.dto.groupbuyingDto.GroupBuyingPostRequest;
import com.godchigam.godchigam.domain.groupbuying.dto.ProductInfo;
import com.godchigam.godchigam.domain.groupbuying.dto.groupbuyingDto.ProductResponse;
import com.godchigam.godchigam.domain.groupbuying.entity.Product;
import com.godchigam.godchigam.domain.groupbuying.repository.ProductRepository;
import com.godchigam.godchigam.domain.groupbuying.service.GroupbuyingService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groupbuying")
public class GroupbuyingController {

    private final JwtTokenProvider jwtTokenProvider;
    private final GroupbuyingService groupbuyingService;
    private final ProductRepository productRepository;


    @PostMapping("")
    public CommonResponse<Product> createProduct(@RequestHeader("token") String accessToken, @RequestBody GroupBuyingPostRequest request) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        Product product = groupbuyingService.createProduct(request, loginId);
        if (product.getWriter().getAddress() == null || product.getWriter().getAddress().isEmpty()) {
            log.info("에러");
            throw new BaseException(ErrorCode.NULL_ADDRESS);
        }
        if (product.getProductName().length() > 16) {
            log.info("에러");
            throw new BaseException(ErrorCode.TOO_LONG_PRODUCT_NAME);
        }
        return CommonResponse.success(null, "같이 구매 등록 성공");
    }

    @PostMapping("/{productId}")
    public CommonResponse<Product> updateProduct(@RequestHeader("token") String accessToken, @RequestBody GroupBuyingPostRequest request,
                                                 @PathVariable("productId") Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            log.info("에러");
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        return CommonResponse.success(groupbuyingService.updateProduct(request, productId), "같이 구매 상품 정보 수정 성공");
    }

    @DeleteMapping("/{productId}")
    public CommonResponse<Product> deleteProduct(@RequestHeader("token") String accessToken, @PathVariable Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            log.info("에러");
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        return CommonResponse.success(groupbuyingService.deleteProduct(productId, loginId), "같이 구매 상품 삭제 성공");
    }

    @GetMapping("/my")
    public CommonResponse<List<ProductInfo>> groupBuyingMy(@RequestHeader("token") String accessToken, @RequestParam String type) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        String message = null;
        if (type.equals("register")) {
            message = "내가 등록한 같이 구매 목록 조회 성공";
        } else if (type.equals("join")) {
            message = "내가 참여한 같이 구매 목록 조회 성공";
        }
        return CommonResponse.success(groupbuyingService.myGroupBuying(loginId, type), message);
    }

    @GetMapping("")
    public CommonResponse<ProductResponse> groupBuyingMain(@RequestHeader("token") String accessToken) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(groupbuyingService.groupBuyingMain(loginId), "같이 구매 메인 조회 성공");
    }

}
