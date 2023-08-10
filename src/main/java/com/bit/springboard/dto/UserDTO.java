package com.bit.springboard.dto;

import com.bit.springboard.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private long id;
    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private String userTel;
    private LocalDateTime userRegdate;
    private String role;
    private String curUserPw;
    private String token;

    public User DTOToEntity() {
        User user = User.builder()
                .id(this.id)
                .userId(this.userId)
                .userPw(this.userPw)
                .userEmail(this.userEmail)
                .userName(this.userName)
                .userTel(this.userTel)
                .userRegdate(LocalDateTime.now())
                .role(this.role)
                .build();

        return user;
    }
}
