package com.cn.tfe.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@SuperBuilder
public class ResponsePage<T> extends ResponseData<T>{

    private int total;
    private int totalPage;

    @Builder.Default()
    private int pageSize = 10;

    public static <T> ResponsePage<List<T>> page(List<T> data,int total,int pageSize){
        if(total<=0)
            return ResponsePage.<List<T>>builder()
                    .data(new ArrayList())
                    .total(0)
                    .totalPage(0)
                    .build();
        int totalPage = total%pageSize == 0? total/pageSize : total/pageSize+1;
        return ResponsePage.<List<T>>builder()
                .data(data)
                .total(total)
                .totalPage(totalPage)
                .build();
    }

}
