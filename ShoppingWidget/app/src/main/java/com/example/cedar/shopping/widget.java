package com.example.cedar.shopping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Implementation of App Widget functionality.
 */
public class widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent jump_GoodsDetail = new Intent(context, GoodsDetail.class);
        PendingIntent to_GoodsDetail = PendingIntent.getActivity(
                context, 0, jump_GoodsDetail, PendingIntent.FLAG_UPDATE_CURRENT
        );
        // Construct the RemoteViews object
        RemoteViews pack_widget =
                new RemoteViews(context.getPackageName(), R.layout.widget);
        pack_widget.setOnClickPendingIntent(R.id.widget, to_GoodsDetail);
        CharSequence widgetText = context.getString(R.string.widgetdefaulttext);
        pack_widget.setTextViewText(R.id.widget_text, widgetText);
        ComponentName cmpnt_widget =
                new ComponentName(context, widget.class);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(cmpnt_widget, pack_widget);
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
        String WIDGET_STATIC = "android.appwidget.action.APPWIDGET_UPDATE";
        final String WIDGET_DYNAMIC = "android.appwidget.action.dynamic.APPWIDGET_UPDATE";
        if(intent.getAction().equals(WIDGET_DYNAMIC)){
//            Toast.makeText(context, "Accept from GoodsDetails", Toast.LENGTH_SHORT)
//                    .show();

            RemoteViews dynamic_widget =
                    new RemoteViews(context.getPackageName(), R.layout.widget);
            Intent from_widget = new Intent(context, MainActivity.class);
            from_widget.addCategory(Intent.CATEGORY_ALTERNATIVE);

//          send information to goodsdetail class, so that we can syns;
            Bundle from_detail = intent.getExtras();
            PendingIntent widget_chge =
                    PendingIntent.getActivity(context, 0, from_widget, PendingIntent.FLAG_UPDATE_CURRENT);

            final int good_id = from_detail.getInt("good_id");
            final int imageId = from_detail.getInt("good_image");
            String name = MainActivity.Name[good_id];
            dynamic_widget.setTextViewText(R.id.widget_text, "Your cart has got "+name+"!");
            dynamic_widget.setImageViewResource(R.id.widget_image, imageId);
            dynamic_widget.setOnClickPendingIntent(R.id.widget, widget_chge);

            ComponentName componentName =
                    new ComponentName(context, widget.class);

            AppWidgetManager appWidgetManager
                    = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(componentName, dynamic_widget);
        }else if(intent.getAction().equals(WIDGET_STATIC)){
            RemoteViews static_widget =
                    new RemoteViews(context.getPackageName(), R.layout.widget);
            Intent cmpnt_widget = new Intent(context, GoodsDetail.class);
            cmpnt_widget.addCategory(Intent.CATEGORY_ALTERNATIVE);

//          send information to goodsdetail class, so that we can syns;
            Bundle from_note = intent.getExtras();
            Bundle notetogoods = new Bundle();
            notetogoods.putInt("good_id", from_note.getInt("note_id"));
            notetogoods.putString("good_name", from_note.getString("note_name"));
            notetogoods.putInt("good_image", from_note.getInt("note_image"));
            notetogoods.putString("good_price", from_note.getString("note_price"));
            notetogoods.putString("good_letter", from_note.getString("note_letter"));
            notetogoods.putString("Information", from_note.getString("note_info"));
            cmpnt_widget.putExtras(notetogoods);
            PendingIntent widget_chge =
                    PendingIntent.getActivity(context, 0, cmpnt_widget, PendingIntent.FLAG_UPDATE_CURRENT);

            Bundle bundle = intent.getExtras();
            final int good_id = bundle.getInt("note_id");
            final int imageId = bundle.getInt("note_image");
            String name = MainActivity.Name[good_id];
            String price = MainActivity.Price[good_id];
            static_widget.setTextViewText(R.id.widget_text, name + " only " + price);
            static_widget.setImageViewResource(R.id.widget_image, imageId);
            static_widget.setOnClickPendingIntent(R.id.widget, widget_chge);

            ComponentName componentName =
                    new ComponentName(context, widget.class);

            AppWidgetManager appWidgetManager
                    = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(componentName, static_widget);
        }
    }
}

