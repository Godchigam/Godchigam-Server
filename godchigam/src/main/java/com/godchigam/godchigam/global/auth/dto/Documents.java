package com.godchigam.godchigam.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Documents {
    @JsonProperty("region_type")
    private String region_type;
    @JsonProperty("address_name")
    private String address_name;
    @JsonProperty("region_1depth_name")
    private String region_1depth_name;
    @JsonProperty("region_2depth_name")
    private String region_2depth_name;
    @JsonProperty("region_3depth_name")
    private String region_3depth_name;
    @JsonProperty("region_4depth_name")
    private String region_4depth_name;
    @JsonProperty("code")
    private String code;
    @JsonProperty("x")
    private String x;

    public String getRegion_type() {
        return region_type;
    }

    public String getAddress_name() {
        return address_name;
    }

    public String getRegion_1depth_name() {
        return region_1depth_name;
    }

    public String getRegion_2depth_name() {
        return region_2depth_name;
    }

    public String getRegion_3depth_name() {
        return region_3depth_name;
    }

    public String getRegion_4depth_name() {
        return region_4depth_name;
    }

    public String getCode() {
        return code;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    @JsonProperty("y")
    private String y;

}
