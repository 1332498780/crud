package com.cn.tfe.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@ToString
@Data
@Document(collection = "user")
public class User {

    @Id
    private String id;

    @Size(min = 6,max = 18,message = "用户名位数需要在6~18位之间")
    @Indexed(unique = true)
    private String username;

    @Size(min = 6,max = 18,message = "密码位数需要在6~18位之间")
    private String password;

    private String name;
}
