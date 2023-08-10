package com.bit.springboard.service.impl;

import com.bit.springboard.entity.User;
import com.bit.springboard.repository.UserRepository;
import com.bit.springboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public User idCheck(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        //아이디가 중복되지 않았으면 null 리턴
        if(userOptional.isEmpty()) {
            return null;
        }

        //아이디가 중복됐으면 User엔티티 리턴
        return userOptional.get();
    }

    @Override
    public User join(User user) {
        if(user == null || user.getUserId() == null) {
            throw new RuntimeException("invalid argument");
        }

        if(userRepository.existsByUserId(user.getUserId())) {
            throw new RuntimeException("already exist id");
        }
        return userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void modify(User modifyUser) {
        userRepository.save(modifyUser);
    }

    @Override
    public User login(String userId, String userPw) {
        Optional<User> loginUser = userRepository.findByUserId(userId);

        if(loginUser.isEmpty()) {
            throw new RuntimeException("id not exist");
        }

        if(!passwordEncoder.matches(userPw, loginUser.get().getUserPw())) {
            throw new RuntimeException("wrong password");
        }

        return loginUser.get();
    }
}
