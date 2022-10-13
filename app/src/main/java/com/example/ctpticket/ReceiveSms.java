package com.example.ctpticket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveSms extends BroadcastReceiver {
    String codBilet = "";

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
}