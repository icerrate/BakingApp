package com.icerrate.bakingapp.view.common;

import android.support.v4.app.Fragment;

/**
 * @author Ivan Cerrate.
 */

public interface BaseFragmentListener {

    void setNavigationToolbar(boolean navigation);

    void enableRotation();

    void replaceFragment(int containerId, Fragment fragment);

    void setTitle(String title);

    void setToolbarVisibility(int visibility);

}
