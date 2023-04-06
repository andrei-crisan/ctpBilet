package com.example.ctpticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    public static EditText mesaj;
    public static TextView codBilet;
    public static TextView continutSms;
    public static TextView ticketValidity;
    public static TextView pretBilet;
    public static TextView senderNr;
    public static ImageView expiratStamp;
    public static SoundPool soundPool;
    public static int notificare;
    public static Boolean arataSms = false;
    public static Button buttonBuy;
    private ValidityManager validityManager = new ValidityManager();

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        mesaj = findViewById(R.id.boxBiletAuto);
        buttonBuy = findViewById(R.id.butonBuy);
        codBilet = findViewById(R.id.codBilet);
        continutSms = findViewById(R.id.continutSms);
        ticketValidity = findViewById(R.id.valabilitateBilet);
        pretBilet = findViewById(R.id.pretBilet);
        senderNr = findViewById(R.id.sender);
        expiratStamp = findViewById(R.id.expirat);

        //Todo: permisiuni de pus intr-o metoda

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1000);
        }

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        SmsManager rcv = new SmsManager();
                        rcv.smsTicketSender(mesaj);

                    } else {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            soundPool = new SoundPool.Builder().setMaxStreams(6).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        notificare = soundPool.load(this, R.raw.notificareblt1, 1);
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
        if (expiratStamp.getVisibility() == View.VISIBLE) {
            expiratStamp.setVisibility(View.GONE);
        }
        this.codBilet.setText(codBilet);
        continutSms.setText(mesajIntegral);
        senderNr.setText("Primit de la: " + msg_from);
        arataSms = true;
        validityManager.startStop();
    }

    public static void playAudio(int nrRepetari) {
        soundPool.play(notificare, 1, 1, 1, nrRepetari, 1);
    }

    public void toggleSms(View view) {
        FrameLayout layoutBilet = findViewById(R.id.layoutBilet);
        FrameLayout layoutSms = findViewById(R.id.layoutSms);

        if (layoutBilet.getVisibility() == View.VISIBLE && arataSms) {
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

    public void updateliniebus(View view) {
        EditText smsBox = findViewById(R.id.boxBiletAuto);
        String valoareLinie = view.getTag().toString();

        if (valoareLinie.startsWith("M")) {
            pretBilet.setText("1,15 € | ⏲");
        } else {
            pretBilet.setText("0,65 € | ⏲");
        }
        if (valoareLinie.equals("25N")) {
            pretBilet.setText("1,00 € | ⏲");
        }
        smsBox.setText(valoareLinie);
    }

    public static void uiTimerUpdate(long timeLeft) {
        int minutes = (int) timeLeft / 60000;
        int seconds = (int) timeLeft % 60000 / 1000;

        String timeLeftUpdate;
        timeLeftUpdate = "" + minutes;
        timeLeftUpdate += ":";
        if (seconds < 10) {
            timeLeftUpdate += "0";
        }
        timeLeftUpdate += seconds;
        ticketValidity.setText(timeLeftUpdate);
    }
}