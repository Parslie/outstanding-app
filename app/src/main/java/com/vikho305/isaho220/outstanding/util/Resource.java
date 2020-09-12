package com.vikho305.isaho220.outstanding.util;

public class Resource<T> {
    private Status status;
    private T data;

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    private Resource(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data);
    }

    public static <T> Resource<T> error(T data) {
        return new Resource<>(Status.ERROR, data);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data);
    }
}
