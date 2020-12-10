package com.cn.tfe.repository;

import com.cn.tfe.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer> {

    User findUsersByUsername(String username);

    User findUserById(String id);
}
