package com.icerrate.bakingapp.view.common;

/**
 * @author Ivan Cerrate.
 */

public abstract class BasePresenter<T extends BaseView> {

    protected T view;

    protected BasePresenter(T view) {
        this.view = view;
    }

    public String getStringRes(int resId) {
        return view.getContext().getString(resId);
    }

    public int getColorRes(int resId) {
        return view.getContext().getResources().getColor(resId);
    }

    public void cancelRequests(){

    }
}
