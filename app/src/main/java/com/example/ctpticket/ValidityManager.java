package com.example.ctpticket;

import static com.example.ctpticket.MainActivity.CHANNEL_ID;

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

public class ValidityManager extends Service {
    private final long TICKET_VALIDITY_IN_SECONDS = 2700000;
    private long timeLeftInSeconds = TICKET_VALIDITY_IN_SECONDS;
    public static boolean timeRunning = false;
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
        }.start();

        timeRunning = true;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("CTP-Bilet")
                .setContentText("Biletul este activ!")
                .setSmallIcon(R.drawable.ic_cumpara)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    public boolean isTimeRunning() {
        return timeRunning;
    }

    public void setTimeRunning(boolean timeRunning) {
        this.timeRunning = timeRunning;
    }

    public CountDownTimer getTicketValidityCounter() {
        return ticketValidityCounter;
    }

    public void setTicketValidityCounter(CountDownTimer ticketValidityCounter) {
        this.ticketValidityCounter = ticketValidityCounter;
    }

    public long getTICKET_VALIDITY_IN_SECONDS() {
        return TICKET_VALIDITY_IN_SECONDS;
    }

    public long getTimeLeftInSeconds() {
        return timeLeftInSeconds;
    }

    public void setTimeLeftInSeconds(long timeLeftInSeconds) {
        this.timeLeftInSeconds = timeLeftInSeconds;
    }
}
