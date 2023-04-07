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
    private SmsBodyValidator validator = new SmsBodyValidator();
    private String ticketValidationCode = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String smsFrom;

            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsFrom = msgs[i].getOriginatingAddress();
                        String messageBody = msgs[i].getMessageBody();

                        Pattern regexTicketPattern = Pattern.compile("[0-9]+\\-[0-9]+[0-9]\\-[0-9]");
                        Matcher regexMatcher = regexTicketPattern.matcher(messageBody);

                        if (regexMatcher.find()) {
                            ticketValidationCode = regexMatcher.group(0);
                        }
                        if (smsFrom.equals("+40740917616") //Verificari autenticitate mesaj;
                                && messageBody.contains("valabil")
                                && messageBody.contains("Cost")) {
                            Toast.makeText(context, "Biletul a fost activat!", Toast.LENGTH_SHORT).show();
                            MainActivity.getInstance().updateCodMesaj(ticketValidationCode, messageBody, smsFrom);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void smsTicketSender(EditText smsBody) {
        String smsTicketDestination = "0740917616";
        String smsTicketBody = smsBody.getText().toString();

        validator.validate(smsTicketBody);

        try {
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            smsManager.sendTextMessage(smsTicketDestination, null, smsTicketBody, null, null);
            Toast.makeText(MainActivity.getInstance(), "SMS-ul a fost trimis!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), "Eroare!", Toast.LENGTH_SHORT).show();
        }
    }
}