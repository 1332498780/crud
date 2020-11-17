package com.cn.tfe.util;

import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@ToString
public class RequestPageData<T> {

    private int page;
    private int size;
    private T data;
    private Sort sort;

    public PageRequest pgData(){
        if(page<0)
            page = 0;
        if(sort == null)
            return PageRequest.of(page,size);
        org.springframework.data.domain.Sort.Direction direction
                = sort.order == Order.desc ? org.springframework.data.domain.Sort.Direction.DESC
                : org.springframework.data.domain.Sort.Direction.ASC;
        return PageRequest.of(page,size,direction,sort.getValue());
    }

    @ToString
    @Data
    public static class Sort{
        Order order;
        String value;
    }

    public enum Order{
        asc,
        desc
    }

}
