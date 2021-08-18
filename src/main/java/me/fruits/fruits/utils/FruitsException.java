package me.fruits.fruits.utils;

import lombok.Data;

/**
 * Java规定：
 * <p>
 * 必须捕获的异常，包括Exception及其子类，但不包括RuntimeException及其子类，这种类型的异常称为Checked Exception。
 * 不需要捕获的异常，包括Error及其子类，RuntimeException及其子类。
 * <p>
 * FruitsException 是项目的异常，继承Exception，抛出该异常必须要铺货
 */

public class FruitsException extends Exception {


    private int errCode;

    public FruitsException(String err){
        super(err);
        this.errCode = ErrCode.DEFAULT_ERR;
    }

    public FruitsException(int errCode, String err) {
        super(err);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
