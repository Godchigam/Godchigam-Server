package com.godchigam.godchigam.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoMapResponse {


    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("documents")
    private List<Documents> documents;

    public Meta getMeta() {
        return meta;
    }

    public List<Documents> getDocuments() {
        return documents;
    }
    public String getAddress() { return  documents.get(0).getAddress_name(); }
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;

        public int getTotalCount() {
            return totalCount;
        }
    }
}
