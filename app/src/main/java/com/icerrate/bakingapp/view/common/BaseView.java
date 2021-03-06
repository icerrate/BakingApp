package com.icerrate.bakingapp.view.common;

import android.content.Context;

/**
 * @author Ivan Cerrate.
 */

public interface BaseView {

    void showProgressBar(boolean show);

    void showError(String errorMessage);

    void showSnackbarMessage(String message);

    Context getContext();
}
