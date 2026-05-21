package com.example.mongrammaire.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mongrammaire.R;

public class ToastHelper {

    public static void showCustomToast(Context context, String message) {
        // Since custom toast views are deprecated for background apps in API 30+, 
        // we use this for foreground cases.
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        ImageView image = layout.findViewById(R.id.toast_logo);
        image.setImageResource(R.drawable.logo);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
