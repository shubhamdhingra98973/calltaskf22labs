package in.dhingra_shubham.calltaskf22labs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Shubham on 11-09-2016.
 */

public class AlertReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager=(NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent pintent=new Intent(context, MainActivity.class);
        pintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context, 0,
                pintent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mbuilder=new NotificationCompat.Builder(context)
                .setContentTitle("Call to your Friend")
                .setTicker("Reminder")
                .setContentText("15 min Later get another Notification")
                .setSmallIcon(R.drawable.phone_call);
        mbuilder.setContentIntent(pendingIntent);
        mbuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mbuilder.setAutoCancel(true);
        notificationManager.notify(0,mbuilder.build());
    }
    }
