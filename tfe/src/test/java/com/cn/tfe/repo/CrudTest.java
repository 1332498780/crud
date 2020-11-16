package com.cn.tfe.repo;


import com.cn.tfe.entity.User;
import com.cn.tfe.repository.crudrepository.CrudUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudTest {

    @Autowired
    CrudUserRepository crudUserRepository;

    @Test
    public void testSql(){
        long res = this.crudUserRepository.count();
        System.out.println(res);
    }

    @Test
    public void testHQL(){
        List<User> users =  this.crudUserRepository.findUserByNameLikeHQL("%三%");
        System.out.println(users);
    }

}
