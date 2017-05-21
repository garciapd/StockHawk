package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.StockProvider;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.MainActivity;

/**
 * Created by danielgarciaperez on 21/05/2017.
 */

public class StockHawkWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_stock_small;
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            views.setTextViewText(R.id.price , "44");
            views.setTextViewText(R.id.symbol, "GOOG");
            views.setTextViewText(R.id.change, "+1%");

            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.linearLayout, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
