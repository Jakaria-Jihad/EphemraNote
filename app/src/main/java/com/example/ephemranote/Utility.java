package com.example.ephemranote;

import android.content.Context;
import android.widget.Toast;

public class Utility {
    static void showToastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
