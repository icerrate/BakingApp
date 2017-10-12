package com.icerrate.bakingapp.view.common;

/**
 * @author Ivan Cerrate.
 */

public interface BaseCallback<T> {

    void onSuccess(T t);

    void onFailure(String errorMessage);
}