package com.example.fastbilling.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.example.fastbilling.R;
import com.example.fastbilling.ui.BillingCheckoutActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class services {

    public static final String FOLDER_NAME = "FastBilling";

    public static String getCurrentDateTime() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cDate);
        return fDate;
    }

    public static String getCurrentDateTimePDF() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(cDate);
        return fDate;
    }

    public static Dialog getAlertDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_dialog_img);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

}
