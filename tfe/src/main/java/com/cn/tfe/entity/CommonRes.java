package com.cn.tfe.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommonRes {

    private String dst; //汉语翻译

    private String phonetic; //汉语拼音

    private String dstTts; //汉语发音

    private String word; //英文
}
