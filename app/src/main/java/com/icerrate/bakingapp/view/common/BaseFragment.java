package com.icerrate.bakingapp.view.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewStub;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.utils.DialogUtils;

import butterknife.BindBool;
import butterknife.BindView;

/**
 * @author Ivan Cerrate.
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    @BindBool(R.bool.is_phone)
    protected boolean isPhone;

    @Nullable
    @BindView(R.id.progress)
    protected ViewStub progressBar;

    protected BaseFragmentListener fragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (BaseFragmentListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentListener = (BaseFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    protected void initPresenter() {
        //Needs to be empty
    }

    protected void saveInstanceState(Bundle outState) {
        //Needs to be empty
    }

    protected void restoreInstanceState(Bundle savedState) {
        //Needs to be empty
    }

    @Override
    public void showProgressBar(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showError(String errorMessage) {
        DialogUtils.createSnackbar(getView(), errorMessage, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void showSnackbarMessage(String message) {
        DialogUtils.createSnackbar(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
