package com.example.bomapetite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bomapetite.R;
import com.example.bomapetite.helper.UsuarioFirebase;
import com.example.bomapetite.model.Empresa;
import com.example.bomapetite.model.Produto;
import com.google.firebase.auth.FirebaseAuth;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);
        getSupportActionBar().setTitle("Novo Produto");

        obterComponentes();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    }

    private void obterComponentes(){
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoDescricao = findViewById(R.id.editProdutoDescricao);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);
    }

    public void validarDadosProduto(View view){
        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();
        String preco = editProdutoPreco.getText().toString();

        if (!nome.isEmpty()){
            if (!descricao.isEmpty()){
                if (!preco.isEmpty()){
                    Produto produto = new Produto();
                    produto.setIdUsuario(idUsuarioLogado);
                    produto.setNome(nome);
                    produto.setDescricao(descricao);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.salvar();
                    finish();
                    exibirMensagem("Produto salvo com sucesso");
                }else{
                    exibirMensagem("Digite o preço do produto");
                }
            }else{
                exibirMensagem("Digite a descrição do produto");
            }
        }else{
            exibirMensagem("Digite o nome do produto");
        }
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
