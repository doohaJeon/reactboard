package com.bit.springboard.oauth.provider;

//여러가지 소셜 로그인을 대응하기 위해 인터페이스로 선언
public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
