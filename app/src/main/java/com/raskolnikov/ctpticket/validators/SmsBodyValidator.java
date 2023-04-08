package com.raskolnikov.ctpticket.validators;

import android.widget.Toast;

import com.raskolnikov.ctpticket.MainActivity;
import com.raskolnikov.ctpticket.exceptions.SmsException;

import java.util.Calendar;

public class SmsBodyValidator {
    private final Calendar greenFriday = Calendar.getInstance();

    public void validate(String smsTicketBody) {
        if (greenFriday.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !smsTicketBody.equals("M40")) {
            Toast.makeText(MainActivity.getInstance(), "@Vineri # Verde", Toast.LENGTH_SHORT).show();
            throw new SmsException("Error type: Green friday!");
        }

        if (smsTicketBody.isEmpty()) {
            Toast.makeText(MainActivity.getInstance(), "Selecteza o linie!", Toast.LENGTH_SHORT).show();
            throw new SmsException("Error type: No line selected!");
        }

        if (Character.isLetter(smsTicketBody.charAt(0))) {
            if (!smsTicketBody.startsWith("M")) {
                Toast.makeText(MainActivity.getInstance(), "Linie invalida!", Toast.LENGTH_SHORT).show();
                throw new SmsException("Error type: Non-urban tickets should start with 'M'");
            }
        }

        if (smsTicketBody.matches("^\\d+")) {
            if (!smsTicketBody.toLowerCase().matches("^\\d+([b|n|l|s|p]+)?$")) {
                Toast.makeText(MainActivity.getInstance(), "Linie Invalida!", Toast.LENGTH_SHORT).show();
                throw new SmsException("Error type: Wrong message pattern!");
            }
        }

//       if (smsTicketBody.matches("^(([a-lA-L|n-zN-Z])+\\d+$)|([a-lA-L|n-zN-Z])+")) {
//           Toast.makeText(MainActivity.getInstance(), "Linia este invalida!", Toast.LENGTH_SHORT).show();
    }
}
