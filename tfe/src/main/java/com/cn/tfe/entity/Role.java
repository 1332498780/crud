package com.cn.tfe.entity;

import lombok.ToString;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private List<User> users;

    @Override
    public String toString(){
        return "{ id: "+id+",roleName:"+roleName+",users:"+ users.size()+" }";
    }
}
