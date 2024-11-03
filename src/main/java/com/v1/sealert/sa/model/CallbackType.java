package com.v1.sealert.sa.model;


public enum CallbackType {
    YES("YES"), NOT("NO");

    private final String value;

    CallbackType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
