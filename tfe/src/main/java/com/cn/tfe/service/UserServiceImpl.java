package com.cn.tfe.service;

import com.cn.tfe.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User findUserById(int id) {
        return new User();
    }

    @Override
    public User findUserByUsername(String username) {
        return new User();
    }
}
