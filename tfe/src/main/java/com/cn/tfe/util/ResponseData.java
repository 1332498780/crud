package com.cn.tfe.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResponseData<T> {

    private int code = 500;
    private T data;

    public static final ResponseData<Map> SUCCESS = ResponseData.<Map>builder().code(200).data(Collections.EMPTY_MAP).build();

    public static <T> ResponseData<T> of(T data){
        return ResponseData.<T>builder().data(data).build();
    }
}
