package com.cn.tfe.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_id_card")
public class IDCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "seq_no")
    private String seqNo;

}
