package com.cn.tfe.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResponseData<T> {

    private int code = 500;
    private T data;

    public static <T> ResponseData<T> of(T data){
        return ResponseData.<T>builder().data(data).build();
    }
}
