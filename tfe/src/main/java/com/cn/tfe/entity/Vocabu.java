package com.cn.tfe.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "vocabulary")
public class Vocabu {

    @Id
    private Integer id;

    private String url;

    private String title;

    private String description;

    //英语单词
    private String word;

    //英文释义
    private String eDict;

    //汉语翻译
    private String dst;

    //汉语拼音
    private String phonetic;






}
