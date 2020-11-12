package com.cn.tfe.service;

import com.cn.tfe.entity.User;

public interface UserService {

    User findUserById(int id);

    User findUserByUsername(String username);
}
