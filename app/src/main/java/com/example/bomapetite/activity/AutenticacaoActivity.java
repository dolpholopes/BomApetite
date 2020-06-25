package com.example.bomapetite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bomapetite.R;

public class AutenticacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();
    }
}
