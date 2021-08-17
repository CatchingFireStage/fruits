package me.fruits.fruits.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

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


    public static Result<String> success(){
        return new Result<>(0, "成功了哇",null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(0, "成功了哇", data);
    }

    public static Result<?> success(long total, long pages, List<?> list) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pages", pages);
        data.put("list", list);
        return new Result<>(0, "", data);
    }

    /**
     * 失败
     */
    public static Result<String> failed(FruitsException exception) {
        return new Result<>(exception.getErrCode(), exception.getMessage(), null);
    }

    public static Result<String> failed(String err) {
        return new Result<>(FruitsException.DEFAULT_ERR, err, null);
    }
}
