package com.bit.springboard.controller;

import com.bit.springboard.dto.ResponseDTO;
import com.bit.springboard.dto.UserDTO;
import com.bit.springboard.entity.User;
import com.bit.springboard.jwt.JwtTokenProvider;
import com.bit.springboard.service.UserService;
import com.bit.springboard.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원정보 수정후 Authentication 객체의 UserDetails를 변경하기 위해
    //loadByUsername 호출
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    @PostMapping("/id-check")
    public ResponseEntity<?> idCheck(@RequestBody UserDTO userDTO) {
        ResponseDTO<Map<String, String>> responseDTO =
                            new ResponseDTO<>();
        System.out.println(userDTO);
        try {
            //중복된 아이디인지 체크
            //중복됐으면 해당 아이디에 대한 유정 정보를 담은 User엔티티가 user 변수에 담김
            //중복되지 않았으면 null이 user 변수에 담김
            User user = userService.idCheck(userDTO.getUserId());

            //메시지를 담을 맵 선언
            Map<String, String> returnMap = new HashMap<>();

            //조건문으로 메시지를 다르게 리턴
            if(user == null) {
                returnMap.put("idCheckMsg", "idOk");
            } else {
                returnMap.put("idCheckMsg", "idFail");
            }

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    
    //회원가입 처리 후 ModelAndView로 login페이지로 이동
    @PostMapping("/join")
    public ResponseEntity<?> join(UserDTO userDTO) {
       ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

       try {
           User user = userDTO.DTOToEntity();

           user.setUserPw(
                   passwordEncoder.encode(userDTO.getUserPw())
           );
           user.setRole("ROLE_USER");

           //회원가입처리(화면에서 보내준 내용을 디비에 저장)
           User joinUser = userService.join(user);
           joinUser.setUserPw("");

           UserDTO joinUserDTO = joinUser.EntityToDTO();

           responseDTO.setItem(joinUserDTO);
           responseDTO.setStatusCode(HttpStatus.OK.value());

           return ResponseEntity.ok().body(responseDTO);
       } catch (Exception e) {
           responseDTO.setErrorMessage(e.getMessage());
           responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
           return ResponseEntity.badRequest().body(responseDTO);
       }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

        try {

            //아이디가 존재하면 해당 아이디에 대한 유저정보가 담김
            //아이디가 존재하지 않으면 null이 담김
            User user = userService.login(userDTO.getUserId(), userDTO.getUserPw());

            if(user != null) {
                String token = jwtTokenProvider.create(user);
                user.setUserPw("");

                UserDTO loginUserDTO = user.EntityToDTO();
                loginUserDTO.setToken(token);

                responseDTO.setItem(loginUserDTO);
                responseDTO.setStatusCode(HttpStatus.OK.value());

                return ResponseEntity.ok().body(responseDTO);
            } else {
                responseDTO.setErrorMessage("login failed");
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }




}









