package com.example.cedar.shopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;


/**
 * Created by Cedar on 2017/10/28.
 */

public class StaticBroadcastReciver extends BroadcastReceiver {
    private static final String STATICATION = "staticbroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(STATICATION)) {

            Bundle bundle = intent.getExtras();

            NotificationManager manager
                    = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);

            Intent To_java = new Intent(context, GoodsDetail.class);

            Bundle notify_goods = new Bundle();
            notify_goods.putInt("good_id", bundle.getInt("note_id"));
            notify_goods.putString("good_name", bundle.getString("note_name"));
            notify_goods.putInt("good_image", bundle.getInt("note_image"));
            notify_goods.putString("good_price", bundle.getString("note_price"));
            notify_goods.putString("good_letter", bundle.getString("note_letter"));
            notify_goods.putString("Information", bundle.getString("note_info"));
            To_java.putExtras(notify_goods);
            PendingIntent Jump_java
                    = PendingIntent.getActivity(context, 0, To_java, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentText(bundle.getString("note_name")+ " sells only "+bundle.getString("note_price"))
                    .setContentTitle("HOT SELL!")
                    .setAutoCancel(true)
                    .setContentIntent(Jump_java)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), bundle.getInt("note_image")))
                    .setSmallIcon(R.mipmap.full_star);

            Notification notify = builder.getNotification();
            manager.notify(0, notify);
        }
    }
}
