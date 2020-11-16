package com.cn.tfe.repository.crudrepository;

import com.cn.tfe.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CrudUserRepository extends CrudRepository<User, Integer> {

    @Query(value = "select u from User u where u.name like :likeName")
    List<User> findUserByNameLikeHQL(String likeName);
}
