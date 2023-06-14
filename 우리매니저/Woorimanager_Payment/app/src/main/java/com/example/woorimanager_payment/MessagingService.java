package com.example.woorimanager_payment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    private final ServerManager serverManager = new ServerManager();
    private int NOTIFICATION_ID = 0, REQUEST_CODE = 0;

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) { // 새 토큰이 생성될 때마다 onNewToken 콜백이 호출됩니다.
        super.onNewToken(token);
        Log.d("FCM_NewToken", "Refreshed token : " + token);

        /**
         * If you want to send messages to this application instance or
         * manage this apps subscriptions on the server side, send the
         * FCM registration token to your app server.
         */
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        serverManager.registerDataToServer(token);
    }

    @Override // FCM에서는 애플리케이션이 서버에서 Push 알림 메시지를 받을 때, 이를 Broadcast 메시지로 전환하여 시스템에 발송
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NOTIFICATION_ID++; REQUEST_CODE++;
            String CHANNEL_ID = "WooriManager", CHANNEL_NAME = "WooriPayment";
            String title = !message.getData().isEmpty() ? message.getData().get("Notice") : message.getNotification().getTitle();
            String body = !message.getData().isEmpty() ? message.getData().get("Text") : message.getNotification().getBody();
            String url = !message.getData().isEmpty() ? message.getData().get("Link") : getString(R.string.defalut_url);

            Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("url", url);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);

            /*안드로이드 오레오(API 26) 이상부터는 모든 알림에 채널을 할당해야한다 채널을 할당하지 않으면 알림이 오지 않는다
            NotificationManager의 createNotificationChannel()를 이용해서 등록, 알림이 올 때마다 만들 필요가 없고 딱 한번만 만들면 된다
            채널은 알림마다 설정해서 채널을 통해 알림을 분류하고 채널별로 설정을 다르게 지정할 수 있다*/
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }
                builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            } else builder = new NotificationCompat.Builder(this);

            builder.setSmallIcon(R.drawable.woorimanager_logo_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.woorimanager_logo_icon))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true);

            Notification notification = builder.build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}