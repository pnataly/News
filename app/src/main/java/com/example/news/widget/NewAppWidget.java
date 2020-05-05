package com.example.news.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.news.R;


public class NewAppWidget extends AppWidgetProvider {
    public static final String ACTION_LINK = "actionLink";
    public static final String ACTION_REFRESH = "actionRefresh";
    public static final String EXTRA_ITEM_LINK = "extraItemLink";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context, NewsService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent refreshIntent = new Intent(context, NewAppWidget.class);
            refreshIntent.setAction(ACTION_REFRESH);
            PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context,0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent clickIntent = new Intent(context, NewAppWidget.class);
            clickIntent.setAction(ACTION_LINK);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            remoteViews.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);
            remoteViews.setRemoteAdapter(R.id.stackView, serviceIntent);
            remoteViews.setEmptyView(R.id.stackView, R.id.widget_empty_view);
            remoteViews.setPendingIntentTemplate(R.id.stackView, clickPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION_LINK.equals(intent.getAction())){
            String link = intent.getStringExtra(EXTRA_ITEM_LINK);

            Uri uri = Uri.parse(link);
            Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
            linkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(linkIntent);
        }
        else if(ACTION_REFRESH.equals(intent.getAction())){
            Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, NewAppWidget.class));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stackView);
        }
        super.onReceive(context,intent);
    }
}

