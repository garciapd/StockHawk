package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;
import com.udacity.stockhawk.ui.MainActivity;

import butterknife.BindView;

/**
 * Created by danielgarciaperez on 22/05/2017.
 */

public class StockHawkWidgetIntentService extends IntentService {

    public StockHawkWidgetIntentService(){
        super("StockHawkWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockHawkWidget.class));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stock_small);

            views.setTextViewText(R.id.widget_name, getString(R.string.app_name));

            views.setRemoteAdapter(R.id.widget_listView, new Intent(this, StockHawkWidgetRemoteViewService.class));

            Intent mainIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingMainIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

            views.setOnClickPendingIntent(R.id.widget_main_bar, pendingMainIntent);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_listView);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

}
