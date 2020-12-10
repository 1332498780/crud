package com.cn.tfe.util.easypoi;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.cn.tfe.entity.CommonRes;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ListCommonResConverter implements Converter<List<CommonRes>> {

    @Override
    public Class supportJavaTypeKey() {
        return CommonRes.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public List<CommonRes> convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String origin = cellData.getStringValue();
        Gson gson = new Gson();
        return gson.fromJson(origin,new TypeToken<List<CommonRes>>(){}.getType());
    }

    @Override
    public CellData convertToExcelData(List<CommonRes> commonRes, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Gson gson = new Gson();
        return new CellData(gson.toJson(commonRes));
    }
}
