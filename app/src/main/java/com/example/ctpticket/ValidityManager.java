package com.example.ctpticket;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ValidityManager {
    private final long TICKET_VALIDITY_IN_SECONDS = 2700000;
    private long timeLeftInSeconds = TICKET_VALIDITY_IN_SECONDS;
    private boolean timeRunning;
    private CountDownTimer count;

    public void startTimer() {
        TextView culoareTimpRamas = MainActivity.getInstance().findViewById(R.id.valabilitateBilet);

        count = new CountDownTimer(timeLeftInSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInSeconds = l;

                if ((int) timeLeftInSeconds / 60000 == 20 && (int) timeLeftInSeconds % 60000 / 1000 == 00) {
                    MainActivity.playAudio(0);
                }
                if ((int) timeLeftInSeconds / 60000 == 10 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    MainActivity.playAudio(1);
                }
                if ((int) timeLeftInSeconds / 60000 == 5 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    culoareTimpRamas.setTextColor(Color.parseColor("#a71919"));
                    MainActivity.playAudio(2);
                }
                if ((int) timeLeftInSeconds / 60000 == 0 && (int) timeLeftInSeconds % 60000 / 1000 == 0) {
                    MainActivity.expiratStamp.setVisibility(View.VISIBLE);
                    MainActivity.playAudio(3);
                }
                MainActivity.uiTimerUpdate(timeLeftInSeconds);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.getInstance(), "Biletul a expirat!", Toast.LENGTH_SHORT).show();
            }
        }.start();
        timeRunning = true;
    }

    public void startStop() {
        if (timeRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void stopTimer() {
        count.cancel();
        timeRunning = false;
        timeLeftInSeconds = TICKET_VALIDITY_IN_SECONDS;
        count.start();
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

    public boolean isTimeRunning() {
        return timeRunning;
    }

    public void setTimeRunning(boolean timeRunning) {
        this.timeRunning = timeRunning;
    }
}
