package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by danielgarciaperez on 21/05/2017.
 */

public class StockHawkWidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }
}
