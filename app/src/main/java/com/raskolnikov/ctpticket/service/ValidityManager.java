package com.raskolnikov.ctpticket.service;

import static com.raskolnikov.ctpticket.MainActivity.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.raskolnikov.ctpticket.MainActivity;
import com.raskolnikov.ctpticket.R;

public class ValidityManager extends Service {
    private final long TICKET_VALIDITY_IN_SECONDS = 2700000;
    private long timeLeftInSeconds = TICKET_VALIDITY_IN_SECONDS;
    private CountDownTimer ticketValidityCounter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        ticketValidityCounter.cancel();
        timeLeftInSeconds = TICKET_VALIDITY_IN_SECONDS;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TextView remainingTimeUntilExpiration = MainActivity.getInstance().findViewById(R.id.valabilitateBilet);
        ticketValidityCounter = new CountDownTimer(timeLeftInSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInSeconds = l;

                if ((int) timeLeftInSeconds / 60000 == 20 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    MainActivity.getInstance().playAudio(0);
                }
                if ((int) timeLeftInSeconds / 60000 == 10 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    MainActivity.getInstance().playAudio(1);
                }
                if ((int) timeLeftInSeconds / 60000 == 5 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    remainingTimeUntilExpiration.setTextColor(Color.parseColor("#a71919"));
                    MainActivity.getInstance().playAudio(2);
                }
                if ((int) timeLeftInSeconds / 60000 == 0 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    MainActivity.getInstance().expirationStampGUI.setVisibility(View.VISIBLE);
                    MainActivity.getInstance().playAudio(3);
                }
                MainActivity.getInstance().uiTimerUpdate(timeLeftInSeconds);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.getInstance(), "Biletul a expirat!", Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        };

        ticketValidityCounter.start();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("CTP-Bilet")
                .setContentText("Biletul este activ!")
                .setSmallIcon(R.drawable.ctp_serv_logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;
    }

}
