package com.godchigam.godchigam.domain.message.dto;
import com.godchigam.godchigam.global.dto.DateInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class SpecificMessageReponse {
    private final String note;
    private final DateInfoResponse dateInfo;
    private final Boolean isReceived;
}
