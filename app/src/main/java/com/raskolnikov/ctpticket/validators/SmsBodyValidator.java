package com.raskolnikov.ctpticket.validators;

import android.widget.Toast;

import com.raskolnikov.ctpticket.MainActivity;

import java.util.Calendar;

public class SmsBodyValidator {
    private final Calendar greenFriday = Calendar.getInstance();

    public void validate(String smsTicketBody) {
        if (greenFriday.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !smsTicketBody.equals("M40")) {
            Toast.makeText(MainActivity.getInstance(), "@Vineri # Verde", Toast.LENGTH_SHORT).show();
            throw new RuntimeException();
        }

        if (smsTicketBody.isEmpty()) {
            Toast.makeText(MainActivity.getInstance(), "Selecteza o linie!", Toast.LENGTH_SHORT).show();
            throw new RuntimeException();
        }



//       if (smsTicketBody.matches("^(([a-lA-L|n-zN-Z])+\\d+$)|([a-lA-L|n-zN-Z])+")) {
//           Toast.makeText(MainActivity.getInstance(), "Linia este invalida!", Toast.LENGTH_SHORT).show();
    }
}
