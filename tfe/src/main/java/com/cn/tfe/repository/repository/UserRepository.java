package com.cn.tfe.repository.repository;

import com.cn.tfe.entity.User;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User,Integer> {

    User findUserById(Integer id);

    List<User> findUsersByNameLike(String name);

    void deleteById(Integer id);
}
