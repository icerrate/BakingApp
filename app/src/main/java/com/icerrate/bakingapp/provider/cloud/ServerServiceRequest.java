package com.icerrate.bakingapp.provider.cloud;

import com.icerrate.bakingapp.view.common.BaseCallback;

import retrofit2.Call;

/**
 * @author Ivan Cerrate.
 */

public class ServerServiceRequest<T> extends ServiceRequest<T> {

    private Call<T> call;

    public ServerServiceRequest(Call<T> call) {
        this.call = call;
    }

    @Override
    void enqueue(BaseCallback<T> baseCallback) {
        call.enqueue(new RetrofitCallback<>(baseCallback));
    }

    @Override
    public void cancel() {
        call.cancel();
    }

}
