package com.cn.tfe.repository.pageRepository;

import com.cn.tfe.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PageUserRepository extends PagingAndSortingRepository<User,Integer> {
}
