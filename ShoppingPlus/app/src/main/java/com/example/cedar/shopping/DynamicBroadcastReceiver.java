package com.example.cedar.shopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Cedar on 2017/10/28.
 */

public class DynamicBroadcastReceiver extends BroadcastReceiver {
    private static final String DYNAMICATION = "dynamicbroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DYNAMICATION)){

            Toast.makeText(context, "DynamicBroadcastReceiver", Toast.LENGTH_SHORT)
                    .show();
            Bundle bundle = intent.getExtras();
            NotificationManager manager
                    = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);

            Intent To_java = new Intent(context, MainActivity.class);
            To_java.putExtra("cart_attitude", false);

            PendingIntent Jump_java
                    = PendingIntent.getActivity(context, 0, To_java, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentText("You have loged "+bundle.getString("good_name"))
                    .setContentTitle("Buy It!")
                    .setTicker("HouHouHouHou")
                    .setAutoCancel(true)
                    .setContentIntent(Jump_java)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), bundle.getInt("good_image")))
                    .setSmallIcon(R.mipmap.empty_star);

            Notification notify = builder.getNotification();
            manager.notify(0, notify);
        }
    }
}
