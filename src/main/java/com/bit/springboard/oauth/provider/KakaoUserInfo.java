package com.bit.springboard.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
    //자원 서버에서 제공한 원시 데이터 형식
    /*
    * {
    *   kakao_account: {
    *       profile: {
    *           nickname: ,
    *       },
    *       email: ,
    *   }
    * }
    * */
    Map<String, Object> attributes;

    //게시판에서 사용할 profile_nickname, account_email
    //kakao_account키로 사용할 정보가 담긴 객체 가져와서 담아줌
    Map<String, Object> properites;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properites = (Map<String, Object>)attributes.get("kakao_account");
    }


    @Override
    public String getProviderId() {
        return this.attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return this.properites.get("email").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>)properites.get("profile");
        return profile.get("nickname").toString();
    }
}
