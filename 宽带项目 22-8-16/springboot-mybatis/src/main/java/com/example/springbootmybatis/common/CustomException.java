package com.example.springbootmybatis.common;

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
