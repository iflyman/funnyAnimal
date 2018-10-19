package com.funnyAnimal.api;

/**
 * Created by bhyan on 2017/6/16.
 */

public class Result<T> {
    public int code = -1;
    public String message;
    public T data;
}
