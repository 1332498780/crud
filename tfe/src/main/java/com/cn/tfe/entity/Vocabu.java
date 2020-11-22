package com.cn.tfe.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "vocabulary")
public class Vocabu {

    @Id
    private String id;

    @NotNull
    private String url;

    @NotNull
    private String title;

    private String description;

    @Indexed(unique = true)
    @NotNull
    private String word; //英语单词

    @NotNull
    private String edict; //英文释义

    private List<String> strokes; //笔画url

    @NotNull
    private CommonRes transRes; //翻译结果集

    private List<CommonRes> exampleRes; //例句结果集

    private List<CommonRes> similarRes; //词组结果集

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date lastModifyTime;

}
