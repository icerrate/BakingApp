package com.icerrate.bakingapp.provider.cloud;

import com.icerrate.bakingapp.view.common.BaseCallback;

/**
 * @author Ivan Cerrate.
 */

public abstract class ServiceRequest<T> {

    abstract void enqueue(BaseCallback<T> doctavioCallback);

    public abstract void cancel();
}
