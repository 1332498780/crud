package com.cn.tfe.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@SuperBuilder
public class ResponsePage<T> extends ResponseData<T>{

    private long total;
    private long totalPage;

    @Builder.Default()
    private int pageSize = 10;


    public static <T> ResponsePage<List<T>> page(List<T> data,long total,int pageSize){
        if(total<=0)
            return ResponsePage.<List<T>>builder()
                    .data(Collections.EMPTY_LIST)
                    .total(0)
                    .totalPage(0)
                    .build();
        long totalPage = total%pageSize == 0? total/pageSize : total/pageSize+1;
        return ResponsePage.<List<T>>builder()
                .data(data)
                .total(total)
                .totalPage(totalPage)
                .build();
    }

    public static <T> ResponsePage<List<T>> page(Page<T> page){
        long total = page.getTotalElements();
        int pagesize = page.getSize();
        List<T> data = page.getContent();
        return page(data,total,pagesize);
    }

}
