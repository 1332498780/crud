package com.cn.tfe.repo;

import com.cn.tfe.entity.User;
import com.cn.tfe.repository.pageRepository.PageUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageTest {

    @Autowired
    PageUserRepository pageUserRepository;

    @Test
    public void testSort(){
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        Sort sort = Sort.by(order);
        Iterable<User> users = this.pageUserRepository.findAll(sort);
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    @Test
    public void testPage(){
        Pageable pageable = PageRequest.of(0,2);
        Page<User> users = this.pageUserRepository.findAll(pageable);

        System.out.println(users.getTotalElements());
        System.out.println(users.getTotalPages());
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }


}
