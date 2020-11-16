package com.cn.tfe.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class TransRes {

    private List<String> dst; //汉语翻译

    private List<String> phonetic; //汉语拼音

    private String eDict; //英文释义

    private String volumnUrl; //发音链接

    private List<String> strokes; //笔画url
}
