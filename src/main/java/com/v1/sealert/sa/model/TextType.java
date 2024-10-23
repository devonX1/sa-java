package com.v1.sealert.sa.model;

public enum TextType {
    START("/start"), GET("/get"), INFO("/info"), CANCEL("/cancel"),
    DISTRICT_CANCEL("/delete_district"), ADD_DISTRICT("/add_district"), GET_DISTRICTS("/get_districts");

    private final String value;

    TextType(String value) {
        this.value = value;
    }
    public String getValue() {
       return this.value;
    }
}
