package com.icerrate.bakingapp.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * @author Ivan Cerrate.
 */

public class HomeWidgetService extends RemoteViewsService {

    public static final String UPDATE_WIDGET = "_update_widget_ingredients";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
