package com.godchigam.godchigam.global.auth;

import com.godchigam.godchigam.global.auth.dto.KakaoMapResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
    public KakaoMapResponse BringAddress(String x,String y) {
        log.info("시작");
        KakaoMapResponse kakaoMapResponse = webClient.get()
                .uri("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x="+x+"&y="+y)
                .header("Authorization","KakaoAK "+key)
                .retrieve()
                .bodyToMono(KakaoMapResponse.class)
                .block();
        return kakaoMapResponse;
    }

}
