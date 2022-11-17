package com.godchigam.godchigam.domain.message.dto;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@NoArgsConstructor
public class MessageRequest {
    private Long[] users;
    private String note;


}
