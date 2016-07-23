package com.aravindputrevu.android.stockhawk.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aravindputrevu.android.stockhawk.R;
import com.aravindputrevu.android.stockhawk.service.StockIntentService;
import com.aravindputrevu.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Aravind on 24-Jun-16.
 */
public class AppWidgetConfigure extends AppWidgetProvider{

    @Override
    public void onEnabled(Context context) {
        initiateService(context);
    }

    private void initiateService(Context context) {
        boolean isConnected = isNetworkAvailable(context);
        // The intent service is for executing immediate pulls from the Yahoo API
        // GCMTaskService can only schedule tasks, they cannot execute immediately
        Intent mServiceIntent = new Intent(context, StockIntentService.class);

        // Run the initialize task service so that some stocks appear upon an empty database
        mServiceIntent.putExtra("tag", "init");
        if (isConnected){
            context.startService(mServiceIntent);
        } else{
            Toast.makeText(context,"No network !",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
     //   initiateService(context);

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MyStocksActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button

           views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
