package com.v1.sealert.sa.model;

public enum TextType {
    START("/start"), GET("/get"), INFO("/info"), CANCEL("/cancel");

    private final String value;

    TextType(String value) {
        this.value = value;
    }
    public String getValue() {
       return this.value;
    }
}
