package com.ahsailabs.beritakita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LatihanUiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan_layout);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, LatihanUiActivity.class);
        context.startActivity(intent);
    }
}