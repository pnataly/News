package com.example.news.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.news.R;
import com.example.news.model.Item;
import com.example.news.util.Util;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;


public class NewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class  WidgetItemFactory implements RemoteViewsFactory {

        private Context context;
        private int appWidgetId;
        private List<Item> itemsList;

        public WidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            new Task().execute();
            SystemClock.sleep(3000);
        }

        @Override
        public void onDataSetChanged() {
            new Task().execute();
            SystemClock.sleep(3000);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            remoteViews.setTextViewText(R.id.title, itemsList.get(position).getTitle());
            remoteViews.setTextViewText(R.id.description, itemsList.get(position).getDescription());
            remoteViews.setTextViewText(R.id.time, itemsList.get(position).getTime());

            Intent linkIntent = new Intent();
            linkIntent.putExtra(NewAppWidget.EXTRA_ITEM_LINK, itemsList.get(position).getLink());
            remoteViews.setOnClickFillInIntent(R.id.description, linkIntent);

            Intent refreshIntent = new Intent();
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            remoteViews.setOnClickFillInIntent(R.id.refresh, refreshIntent);

            SystemClock.sleep(500);
           return remoteViews;
        }


        @Override
        public RemoteViews getLoadingView() {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            remoteViews.setTextViewText(R.id.title, "Loading...");
            remoteViews.setTextViewText(R.id.description, "...");
            remoteViews.setTextViewText(R.id.time, "");
            return remoteViews;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        public class Task extends AsyncTask<Integer, Void, Boolean> {

            @Override
            protected Boolean doInBackground(Integer... integers) {

                try {

                    URL url = new URL(Util.RSS_URL);
                    InputStream inputStream = Util.getInputStream(url);
                    if(inputStream != null){
                        itemsList = Util.parseFeed(inputStream, Util.WIDGET_MODE);
                        Collections.sort(itemsList, Collections.<Item>reverseOrder());
                        return true;
                    }
                    return false;
                }
                catch (MalformedURLException e){
                    e.printStackTrace();
                }
                catch (XmlPullParserException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess) {
                super.onPostExecute(isSuccess);
                if(isSuccess){

                }
            }
        }
    }

}


