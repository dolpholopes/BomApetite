package com.example.bomapetite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bomapetite.R;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);
        getSupportActionBar().setTitle("Novo Produto");
    }
}
