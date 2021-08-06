package me.fruits.fruits.utils;

import lombok.Data;

@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> Result<T> success(T data) {
        return new Result(0, "", data);
    }

    /**
     * 失败
     */
    public static Result failed(FruitsException exception) {
        return new Result(exception.getErrCode(), exception.getMessage(), null);
    }
}
