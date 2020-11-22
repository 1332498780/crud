package com.cn.tfe.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cn.tfe.entity.CommonRes;
import com.cn.tfe.util.easypoi.CommonResConverter;
import com.cn.tfe.util.easypoi.ListCommonResConverter;
import com.cn.tfe.util.easypoi.ListStringConverter;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VocabuDto {

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("自定义URL")
    private String url;

    @ExcelProperty("单词")
    private String word; //英语单词

    @ExcelProperty(value = "英文释义")
    private String edict; //英文释义

    @ExcelProperty(value = "笔画GIF图片URL",converter = ListStringConverter.class)
    private List<String> strokes; //笔画url

    @ExcelProperty(value = "翻译结果集",converter = CommonResConverter.class)
    private CommonRes transRes; //翻译结果集

    @ExcelProperty(value = "例句结果集",converter = ListCommonResConverter.class)
    private List<CommonRes> exampleRes; //例句结果集

    @ExcelProperty(value = "词组结果集",converter = ListCommonResConverter.class)
    private List<CommonRes> similarRes; //词组结果集
}
