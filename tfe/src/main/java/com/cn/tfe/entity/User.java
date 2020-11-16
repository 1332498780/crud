package com.cn.tfe.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@ToString
@Data
@Document(collection = "user")
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private String name;

}
