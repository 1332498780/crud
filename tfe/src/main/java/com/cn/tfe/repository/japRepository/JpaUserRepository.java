package com.cn.tfe.repository.japRepository;

import com.cn.tfe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User,Integer> {

    @Query(value = "select u from User u where u.name like :likeName")
    List<User> findUserByNameLikeHQL(String likeName);

    @Query(value = "select u.* from t_user u where u.name like ? limit ?",nativeQuery = true)
    List<User> findUserByNameLikeSQL(String name,Integer limit);
}
