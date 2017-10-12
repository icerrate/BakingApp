package com.icerrate.bakingapp.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.icerrate.bakingapp.R;

/**
 * @author Ivan Cerrate.
 */

public class DialogUtils {

    public static AlertDialog createErrorDialog(final Activity activity, String errorMessage) {
        String title = activity.getString(R.string.error_title);
        return createAlertDialog(activity, title, errorMessage);
    }

    public static AlertDialog createAlertDialog(Activity activity, String title, String message) {
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm_button, null)
                .create();
    }

    public static Snackbar createSnackbar(View parent, String message, int duration) {
        return Snackbar.make(parent, message, duration)
                .setActionTextColor(parent.getResources().getColor(R.color.colorPrimary));
    }
}
