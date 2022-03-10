package com.spotmoney.model.response;

import java.util.function.Function;

/**
 * Class used for functional programming
 * @param <T>
 */
public class ErrorOrSuccess<T> {

    private final SpotMoneyDemoError error;
    private final T success;

    private ErrorOrSuccess(SpotMoneyDemoError error, T success){
        this.error = error;
        this.success = success;
    }

    public static <T> ErrorOrSuccess<T> error(SpotMoneyDemoError error){
        return  new ErrorOrSuccess<>(error, null);
    }

    public static <T> ErrorOrSuccess<T> success(T success){
        return  new ErrorOrSuccess<>(null, success);
    }

    public boolean isSuccess(){
        return success != null;
    }

    public boolean isError(){
        return success != null;
    }

    public T getSuccess() {
        return success;
    }

    public SpotMoneyDemoError getError() {
        return error;
    }

    public <R> ErrorOrSuccess<R> applyIfSuccess(Function<T, ErrorOrSuccess<R>> f){
        if(this.isSuccess()){
            return f.apply(success);
        }
        return ErrorOrSuccess.error(error);
    }
}
