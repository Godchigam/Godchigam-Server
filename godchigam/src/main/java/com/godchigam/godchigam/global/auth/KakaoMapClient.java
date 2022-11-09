package com.godchigam.godchigam.global.auth;

import com.godchigam.godchigam.global.auth.dto.KakaoMapResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMapClient {
    @Value("${secret.api-key}")
    private String key;
    private final WebClient webClient;


    /**
     * https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=127.1086228&y=37.4012191
     */
    public KakaoMapResponse BringAddress(String x, String y) {
        KakaoMapResponse kakaoMapResponse;
        try {
            kakaoMapResponse = webClient.get()
                    .uri("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=" + x + "&y=" + y)
                    .header("Authorization", "KakaoAK " + key)
                    .retrieve()
                    .bodyToMono(KakaoMapResponse.class)
                    .block();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.WRONG_ADDRESS);
        }

        return kakaoMapResponse;
    }

}
