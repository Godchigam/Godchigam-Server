package com.godchigam.godchigam.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class AddressResponse {
    private final String address_name;
}
