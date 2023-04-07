package com.example.ctpticket;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "ValidityManager";
    private static MainActivity instance;
    public EditText smsInputContentGUI;
    public TextView ticketValidationCodeGUI;
    public TextView smsContentGUI;
    public TextView ticketValidityGUI;
    public TextView ticketPriceGUI;
    public TextView senderNrGUI;
    public ImageView expirationStampGUI;
    public SoundPool soundPool;
    public int notification;
    public Boolean showSms = false;
    public Button buttonBuyTicketGUI;
    private ValidityManager validityManager = new ValidityManager();

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        setContentView(R.layout.activity_main);
        instance = this;

        smsInputContentGUI = findViewById(R.id.boxBiletAuto);
        buttonBuyTicketGUI = findViewById(R.id.butonBuy);
        ticketValidationCodeGUI = findViewById(R.id.codBilet);
        smsContentGUI = findViewById(R.id.continutSms);
        ticketValidityGUI = findViewById(R.id.valabilitateBilet);
        ticketPriceGUI = findViewById(R.id.pretBilet);
        senderNrGUI = findViewById(R.id.sender);
        expirationStampGUI = findViewById(R.id.expirat);

        //Todo: permisiuni de pus intr-o metoda

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1000);
        }


        buttonBuyTicketGUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        SmsManager rcv = new SmsManager();
                        try {
                            rcv.smsTicketSender(smsInputContentGUI);
                        } catch (Exception e){ //Todo: Custom Exception
                            e.printStackTrace();
                        }

                    } else {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        notification = soundPool.load(this, R.raw.notificareblt1, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissionss denied!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    public void updateCodMesaj(String codBilet, String mesajIntegral, String msg_from) {
        if (expirationStampGUI.getVisibility() == View.VISIBLE) {
            expirationStampGUI.setVisibility(View.GONE);
        }

        this.ticketValidationCodeGUI.setText(codBilet);
        smsContentGUI.setText(mesajIntegral);
        senderNrGUI.setText("Primit de la: " + msg_from);
        showSms = true;
        startService();
    }

    public void playAudio(int nrRepetari) {
        soundPool.play(notification, 1, 1, 1, nrRepetari, 1);
    }

    public void toggleSms(View view) {
        FrameLayout layoutBilet = findViewById(R.id.layoutBilet);
        FrameLayout layoutSms = findViewById(R.id.layoutSms);

        if (layoutBilet.getVisibility() == View.VISIBLE && showSms) {
            layoutBilet.setVisibility(View.GONE);
            layoutSms.setVisibility(View.VISIBLE);
        } else {
            layoutSms.setVisibility(View.GONE);
            layoutBilet.setVisibility(View.VISIBLE);
        }
    }

    public void toggleBus(View view) {
        GridLayout urbanGrid = findViewById(R.id.urbanGrid);
        GridLayout metroGrid = findViewById(R.id.metroGrid);
        LinearLayout urbanLinear = findViewById(R.id.bileteLiniiUrbane);
        LinearLayout metroLinear = findViewById(R.id.bileteLiniiMetro);
        TextView textMetroLinear = findViewById(R.id.textBileteLiniiMetro);
        TextView textUrbanLinear = findViewById(R.id.textBileteLiniiUrban);

        if (urbanGrid.getVisibility() == View.VISIBLE) {
            urbanGrid.setVisibility(View.GONE);
            metroGrid.setVisibility(View.VISIBLE);
            metroLinear.setBackgroundColor(Color.parseColor("#FFFFFF"));
            textMetroLinear.setTextColor(Color.parseColor("#000000"));
            urbanLinear.setBackgroundColor(Color.parseColor("#5f3960"));
            textUrbanLinear.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            urbanGrid.setVisibility(View.VISIBLE);
            metroGrid.setVisibility(View.GONE);
            textUrbanLinear.setTextColor(Color.parseColor("#000000"));
            urbanLinear.setBackgroundColor(Color.parseColor("#FFFFFF"));
            metroLinear.setBackgroundColor(Color.parseColor("#5f3960"));
            textMetroLinear.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void updateBusLineGUI(View view) {
        EditText smsBox = findViewById(R.id.boxBiletAuto);
        String valoareLinie = view.getTag().toString();

        if (valoareLinie.startsWith("M")) {
            ticketPriceGUI.setText("1,15 € | ⏲");
        } else {
            ticketPriceGUI.setText("0,65 € | ⏲");
        }
        if (valoareLinie.equals("25N")) {
            ticketPriceGUI.setText("1,00 € | ⏲");
        }
        smsBox.setText(valoareLinie);
    }

    public void uiTimerUpdate(long timeLeft) {
        int minutes = (int) timeLeft / 60000;
        int seconds = (int) timeLeft % 60000 / 1000;

        String timeLeftUpdate;
        timeLeftUpdate = "" + minutes;
        timeLeftUpdate += ":";
        if (seconds < 10) {
            timeLeftUpdate += "0";
        }
        timeLeftUpdate += seconds;
        ticketValidityGUI.setText(timeLeftUpdate);
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "Exemplu", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void startService() {
        Intent intent = new Intent(this, ValidityManager.class);
        stopService(intent);
        startService(intent);
    }

}