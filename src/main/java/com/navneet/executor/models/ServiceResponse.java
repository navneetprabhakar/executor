package com.navneet.executor.models;

import lombok.Data;

@Data
public class ServiceResponse<T> {
    private String status;
    private String message;
    private T data;
}
