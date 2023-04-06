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
    private CountDownTimer ticketValidityCounter;


    public void startTimer() {
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
        ticketValidityCounter.cancel();
        timeRunning = false;
        timeLeftInSeconds = TICKET_VALIDITY_IN_SECONDS;
        ticketValidityCounter.start();
    }

}