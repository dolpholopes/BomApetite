package com.example.bomapetite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bomapetite.R;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);
        getSupportActionBar().setTitle("Configurações");
    }
}
