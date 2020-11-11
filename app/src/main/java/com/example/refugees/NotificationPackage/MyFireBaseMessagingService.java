package com.example.refugees.NotificationPackage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.refugees.MainScreenActivity;
import com.example.refugees.MainScreenFragments.NotificationFragment;
import com.example.refugees.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Title");
        message=remoteMessage.getData().get("Message");

        // TODO: handle notification language when create sharedPreference
        boolean isAR = getBaseContext().getResources().getConfiguration().locale.getLanguage().equals("ar");
        String lang = getBaseContext().getResources().getConfiguration().locale.getLanguage();
        if(message.equals("SR")){
            if(!isAR) {
                Log.d("lang_tracker", "here "+ isAR + " "+ lang);
                message = title + " request to show your information.";
                title = "Information Request";
            } else {
                Log.d("lang_tracker", "here "+ isAR);
                message =" يطلب استعراض بياناتك الشخصية." + title;
                title = "طلب بيانات";
            }
        } else {
            if(!isAR) {
                message = title + " accept your details request.";
                title = "Accepted Request";
            } else {
                message =" قام بقبول طلبك لإستعراض بياناتة الشخصية." + title;
                title = "قبول طلب";
            }
        }

        Log.d("notification_tracker", "received " + title);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resIntent = new Intent(this, MainScreenActivity.class);
        PendingIntent resPendingIntent = PendingIntent.getActivity(this, 1, resIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel reqChannel;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            reqChannel = new NotificationChannel("REQUEST_CHANNEL", "request channel", NotificationManager.IMPORTANCE_HIGH);
            reqChannel.setDescription("This is channel for handle details request.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(reqChannel);
            Log.d("notification_tracker", "Channel created " + reqChannel.getId());
        }

        @SuppressLint("WrongConstant")
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "REQUEST_CHANNEL")
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(icon)
                        .setSound(sound)
                        .setBadgeIconType(R.mipmap.ic_launcher_round)
                        .setContentIntent(resPendingIntent);
        Log.d("notification_tracker", "build " + title);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        Log.d("notification_tracker", "shows " + title);
    }

}