package com.bit.springboard.jwt;


import com.bit.springboard.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* 요청이 왔을 때 헤더에 담긴 JWT Token을 받아서 유효성 검사를 하고
* Token안에 있는 username을 리턴하기 위한 필터 클래스
* SecuriryConfiguration에 filter로 등록돼서 인증이 필요한 요청이 올 때마다
* 자동실행되도록 설정
* */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;



    //filter로 등록하면 자동으로 실행될 메소드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            //request에서 token 꺼내오기
            //token 값이 있으면 토큰값이 담기고 토큰 값이 없으면 null이 담긴다.
            String token = parseBearerToken(request);

            //토큰 검사 및 시큐리티 등록
            if (token != null && !token.equalsIgnoreCase("null")) {
                //유효성 검사 및 username가져오기
                String userId = jwtTokenProvider.validateAndGetUsername(token);

                UserDetails userDetails =
                        userDetailsServiceImpl.loadUserByUsername(userId);

                //유효성 검사 완료된 토큰 시큐리티에 인증된 사용자로 등록
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            System.out.println("set security context error" + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        /*
        * 넘어오는 토큰의 형태
        * header: {
        *           Authorization: "Bearer 토큰값"
        *         }
        * */
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            //실제 token의 값만 리턴
            return bearerToken.substring(7);
        }

        return null;
    }
}
