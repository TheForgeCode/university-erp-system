package edu.univ.erp.util;

public class Result<T> {
    public final boolean ok;
    public final String message;
    public final T data;
    private Result(boolean ok, String message, T data) {
        this.ok = ok; this.message = message; this.data = data;
    }
    public static <T> Result<T> ok(T data) { return new Result<>(true, null, data); }
    public static <T> Result<T> fail(String msg) { return new Result<>(false, msg, null); }
}
