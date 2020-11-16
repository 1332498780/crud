package com.cn.tfe.repo;


import com.cn.tfe.entity.User;
import com.cn.tfe.repository.japRepository.JpaUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JapTest {

    @Autowired
    JpaUserRepository jpaUserRepository;
    @Test
    public void testJpaHQL(){
        List<User> users = this.jpaUserRepository.findUserByNameLikeHQL("%三%");
        System.out.println(users);
    }

    @Test
    public void testJpaSQL(){
        List<User> users = this.jpaUserRepository.findUserByNameLikeSQL("%三%",1);
        System.out.println(users);
    }
}
