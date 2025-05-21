package com.example.work_school.repository;

public interface IApiCallback<T> {
    void onSuccess(T result);

    void onError(String errorMessage);
}
