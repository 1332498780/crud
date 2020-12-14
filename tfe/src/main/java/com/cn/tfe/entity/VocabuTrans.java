package com.cn.tfe.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Document(collection = "vocabuTrans")
@CompoundIndex(name = "word_fromTo_index", def = "{'word': 1, 'fromTo': 1}",unique = true)
public class VocabuTrans{

    @NotNull
    private Integer fromTo;

    @Id
    private String id;

    private String url;

    private String title;

    private String description;

    @NotNull
    private String word; //英语单词

    private String edict; //英文释义

    private List<String> strokes; //笔画url

    private CommonRes transRes; //翻译结果集

    private List<CommonRes> exampleRes; //例句结果集

    private List<CommonRes> similarRes; //词组结果集

    private List<AdverLink> adverLinks;
}
