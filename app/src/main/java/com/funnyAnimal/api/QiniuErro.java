package com.funnyAnimal.api;

/**
 * Created by 青峰 on 2017/7/22.
 */

public class QiniuErro extends Throwable {

    public int statusCode;
    public String error;

    public QiniuErro(int code, String error) {
        this.statusCode = code;
        this.error = error;
    }
}
