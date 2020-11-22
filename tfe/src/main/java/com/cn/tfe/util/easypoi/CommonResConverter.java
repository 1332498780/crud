package com.cn.tfe.util.easypoi;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.cn.tfe.entity.CommonRes;
import com.google.gson.Gson;

public class CommonResConverter implements Converter<CommonRes> {
    @Override
    public Class supportJavaTypeKey() {
        return CommonRes.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public CommonRes convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String origin = cellData.getStringValue();
        Gson gson = new Gson();
        return gson.fromJson(origin,CommonRes.class);
    }

    @Override
    public CellData convertToExcelData(CommonRes commonRes, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Gson gson = new Gson();
        return new CellData(gson.toJson(commonRes));
    }
}
