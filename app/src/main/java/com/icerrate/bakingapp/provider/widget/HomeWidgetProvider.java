package com.icerrate.bakingapp.provider.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.icerrate.bakingapp.R;
import com.icerrate.bakingapp.provider.preference.BakingAppPreferences;
import com.icerrate.bakingapp.service.HomeWidgetService;
import com.icerrate.bakingapp.utils.InjectionUtils;

/**
 * @author Ivan Cerrate.
 */

public class HomeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, HomeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                    R.layout.widget_home);
            remoteView.setEmptyView(R.id.widget_ingredients_list, R.id.widget_ingredients_no_data);
            remoteView.setRemoteAdapter(R.id.widget_ingredients_list, intent);
            BakingAppPreferences bakingAppPreferences = InjectionUtils.bakingAppPreferences(context);
            String recipeName = bakingAppPreferences.getWidgetIdRecipeName(appWidgetId);
            if (recipeName!= null) {
                remoteView.setTextViewText(R.id.widget_title, recipeName);
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_title);
            appWidgetManager.updateAppWidget(appWidgetId, remoteView);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName widget = new ComponentName(context, HomeWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(widget);
        switch (intent.getAction()) {
            case HomeWidgetService.UPDATE_WIDGET: case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_ingredients_list);
                this.onUpdate(context, appWidgetManager, ids);
                break;
        }
    }
}
