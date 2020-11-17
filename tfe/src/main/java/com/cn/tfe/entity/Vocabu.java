package com.cn.tfe.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@Builder
@ToString
@Document(collection = "vocabulary")
public class Vocabu {

    @Id
    private Integer id;

    private String url;

    private String title;

    private String description;

    @Indexed(unique = true)
    private String word; //英语单词

    private List<TransRes> transRes; //翻译结果集

    private List<CommonRes> wordRes; //词组结果集

    private List<CommonRes> sentenceRes; //例句结果集

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date lastModifyTime;

}
