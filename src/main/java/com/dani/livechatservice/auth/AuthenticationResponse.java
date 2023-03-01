package com.dani.livechatservice.auth;

import lombok.Builder;

import java.util.Date;

@Builder
public record AuthenticationResponse(String token, Date expiration, String refreshToken, Date refreshTokenExpiration){}
