package com.bit.springboard.service;

import com.bit.springboard.entity.User;

public interface UserService {
    User idCheck(String userId);

    User join(User user);

    User findById(long id);

    void modify(User modifyUser);

    User login(String userId, String userPw);
}
