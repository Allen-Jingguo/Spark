
package com.ssc.ssgm.fx.ifx.integration.common;

public class Response<T> {

//    success: boolean; // if request is success
//    data?: any; // response data
//    errorCode?: string; // code for errorType
//    errorMessage?: str

    private boolean success;

    private T data;

    private String errorMessage;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Response(boolean success, String errorMessage, T data) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public static Response<Boolean> success() {
        return new Response<>(true, null, null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, RespCode.SUCCESS.getDesc(), data);
    }

    public static Response<Boolean> fail(String msg) {
        return new Response<>(false, msg, null);
    }

    public static Response<Boolean> fail() {
        return new Response<>(false, RespCode.FAIL.getDesc(), null);
    }

    public static Response<Boolean> unlogin() {
        return new Response<>(false, RespCode.UN_LOGIN.getDesc(), null);
    }

}
