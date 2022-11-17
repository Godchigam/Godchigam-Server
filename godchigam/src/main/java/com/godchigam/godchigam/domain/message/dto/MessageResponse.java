package com.godchigam.godchigam.domain.message.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class MessageResponse {
    private final Long noteId;
    private final FriendInfo friendInfo;
    private final String recentNote;
}
