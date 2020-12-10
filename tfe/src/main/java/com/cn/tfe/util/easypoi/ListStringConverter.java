package com.cn.tfe.util.easypoi;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ListStringConverter implements Converter<List<String>> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public List<String> convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String origin = cellData.getStringValue();
        Gson gson = new Gson();
        return gson.fromJson(origin,new TypeToken<List<String>>(){}.getType());
    }

    @Override
    public CellData convertToExcelData(List<String> strings, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Gson gson = new Gson();
        return new CellData(gson.toJson(strings));
    }
}
