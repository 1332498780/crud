package com.cn.tfe.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommonRes {

    private String dst; //汉语翻译

    private List<String> phonetic; //汉语拼音

    private String eDict; //英文释义
}
