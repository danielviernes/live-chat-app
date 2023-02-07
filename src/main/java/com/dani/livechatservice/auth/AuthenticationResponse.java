package com.dani.livechatservice.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token){}
