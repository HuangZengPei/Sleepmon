package com.apm.sleepmon.Receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.apm.sleepmon.Fragments.MusicFragment;
import com.apm.sleepmon.MainActivity;
import com.apm.sleepmon.R;

/**
 * Implementation of App Widget functionality.
 */
public class MusicWidget extends AppWidgetProvider {

    static PendingIntent getPI(Context context, int viewID) {
        Intent intent = new Intent(context, MusicWidget.class);
        intent.setData(Uri.parse("" + viewID));
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_widget);
        views.setOnClickPendingIntent(R.id.pause2, getPI(context, R.id.pause2));
//        views.setOnClickPendingIntent(R.id.stop2, getPI(context, R.id.stop2));
        views.setOnClickPendingIntent(R.id.last_music, getPI(context, R.id.last_music));
        views.setOnClickPendingIntent(R.id.next_music, getPI(context, R.id.next_music));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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
        super.onReceive(context, intent);
        if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            String data = intent.getData().toString();
            int resID = -1;
            if (data != null) {
                resID = Integer.parseInt(data);
            }
            switch (resID) {
                case R.id.pause2:
                    musicOption(context, 0);
                    break;
/*                case R.id.stop2:
                    musicOption(context, 1);
                    break;*/
                case R.id.next_music:
                    musicOption(context, 2);
                    break;
                case R.id.last_music:
                    musicOption(context, 3);
                    break;
            }
        }
        else if (intent.getAction().equals(MusicFragment.changeWidget)) {
            String source = intent.getStringExtra("source");
            boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
            String x;
            if (isPlaying) {
                x = "暂停";
            }
            else {
                x = "播放";
            }
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.music_widget);
            remoteViews.setTextViewText(R.id.appwidget_text, source);
            remoteViews.setTextViewText(R.id.pause2, x);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, MusicWidget.class);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }
    }
    private void musicOption(Context context, int ACTION) {
        Intent intent = new Intent(MusicFragment.musicWidget);
        intent.putExtra("musicOption", ACTION);
        context.sendBroadcast(intent);
        Log.i("musicOption", "sendBroadcast");
    }
}

