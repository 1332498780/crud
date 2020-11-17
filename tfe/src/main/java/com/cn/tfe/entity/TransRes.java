package com.cn.tfe.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@SuperBuilder
public class TransRes extends CommonRes{

    private String volumnUrl; //发音链接

    private List<String> strokes; //笔画url
}
