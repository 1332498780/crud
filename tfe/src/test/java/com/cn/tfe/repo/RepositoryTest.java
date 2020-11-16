package com.cn.tfe.repo;

import com.cn.tfe.entity.Role;
import com.cn.tfe.entity.User;
import com.cn.tfe.repository.repository.RoleRepository;
import com.cn.tfe.repository.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testCasecade(){
        User user = this.userRepository.findUserById(1);
        System.out.println(user);
        Assert.assertNotNull(user);
    }

    @Test
    public void testCasecadeDel(){
        this.userRepository.deleteById(2);
    }

    @Test
    public void testCasecadeDel1(){
        this.userRepository.deleteById(2);
    }

    @Test
    public void testRoleList(){
        List<Role> roles = this.roleRepository.findRolesByIdAfter(1);
        System.out.println(roles);
    }

    @Test
    public void testRoleDel(){
        this.roleRepository.deleteById(3);
    }
}
