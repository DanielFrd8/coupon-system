package com.dani.couponsystemv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDto<T> {

    private boolean success;
    private T content;
    private LocalDateTime dateTime;

    public static <T> ResponseDto<T> of(boolean success,T content){
        return new ResponseDto<>(success,content,LocalDateTime.now());
    }

    public static <T> ResponseDto<T> success(T content){
        return ResponseDto.of(true,content);
    }

    public static <T> ResponseDto<T> failure(T content){
        return ResponseDto.of(false,content);
    }
}