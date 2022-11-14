package com.godchigam.godchigam.domain.groupbuying.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupBuyingPostRequest {
    private String productImageUrl;
    private String productName;
    private Integer productPrice;
    private String category;
    private Integer goalPeopleCount;
    private String dealingMethod;
    private String description;
}
