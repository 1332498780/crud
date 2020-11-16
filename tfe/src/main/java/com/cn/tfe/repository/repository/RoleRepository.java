package com.cn.tfe.repository.repository;

import com.cn.tfe.entity.Role;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RoleRepository extends Repository<Role,Integer> {

    List<Role> findRolesByIdAfter(Integer id);

    void deleteById(Integer id);
}
