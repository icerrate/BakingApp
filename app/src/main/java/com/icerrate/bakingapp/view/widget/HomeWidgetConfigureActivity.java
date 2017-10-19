package com.icerrate.bakingapp.view.widget;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.view.common.BaseActivity;

/**
 * @author Ivan Cerrate.
 */

public class HomeWidgetConfigureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setNavigationToolbar(true);
        if (savedInstanceState == null) {
            Integer widgetId = getIntent().getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            HomeWidgetConfigureFragment homeWidgetConfigureFragment = HomeWidgetConfigureFragment.newInstance(widgetId);
            replaceFragment(R.id.content, homeWidgetConfigureFragment);
        }
    }
}
