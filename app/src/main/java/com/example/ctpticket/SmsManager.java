package com.example.ctpticket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsManager extends BroadcastReceiver {
   private String codBilet = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;

            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String mesajIntegral = msgs[i].getMessageBody();

                        Pattern patternBilet = Pattern.compile("[0-9]+\\-[0-9]+[0-9]\\-[0-9]");
                        Matcher match = patternBilet.matcher(mesajIntegral);

                        if (match.find()) { //verific patern in mesaj
                            codBilet = match.group(0);
                        }
                        if (msg_from.equals("+40740917616") && mesajIntegral.contains("valabil") && mesajIntegral.contains("Cost")) { //daca mesaj e de la nr nostru
                            Toast.makeText(context, "Biletul a fost activat!", Toast.LENGTH_SHORT).show();
                            MainActivity.getInstance().updateCodMesaj(codBilet, mesajIntegral, msg_from);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void smsTicketSender(EditText mesaj) {
        Calendar ziuaDeVineri = Calendar.getInstance();
        String destinatieCtp = "0740917616";
        String sms = mesaj.getText().toString();

        if (ziuaDeVineri.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !sms.contains("M40")) {
            Toast.makeText(MainActivity.getInstance(), "Azi nu-i nevoie de bilet, numa' in sat! @metropolitan", Toast.LENGTH_SHORT).show();
        }
        if(sms.matches("^(([a-lA-L|n-zN-Z])+\\d+$)|([a-lA-L|n-zN-Z])+")){
            Toast.makeText(MainActivity.getInstance(), "Linia este invalida!", Toast.LENGTH_SHORT).show();
        }else {
            try {
                android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                if(!sms.isEmpty()){
                    smsManager.sendTextMessage(destinatieCtp, null, sms, null, null);
                    Toast.makeText(MainActivity.getInstance(), "SMS-ul a fost trimis!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.getInstance(), "Selecteaza o linie de autobuz!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.getInstance(), "Eroare!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}