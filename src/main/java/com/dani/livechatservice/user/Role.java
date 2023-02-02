package com.dani.livechatservice.user;

public enum Role {

    MOD("Moderator", 1),
    USER("User", 2),

    ;
    private String role;
    private Integer code;

    Role(String role, Integer code) {
        this.role = role;
        this.code = code;
    }

    public String getRole() {
        return this.role;
    }

    public Integer getCode() {
        return this.code;
    }
}
