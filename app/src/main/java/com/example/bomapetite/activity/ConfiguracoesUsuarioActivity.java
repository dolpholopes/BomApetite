package com.example.bomapetite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bomapetite.R;
import com.example.bomapetite.helper.ConfiguracaoFirebase;
import com.example.bomapetite.helper.UsuarioFirebase;
import com.example.bomapetite.model.Empresa;
import com.example.bomapetite.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome, editUsuarioEndereco;
    private String idUsuario;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);
        getSupportActionBar().setTitle("Configurações");

        inicializaComponentes();
        idUsuario = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        recuperarDadosUsuario();
    }

    public void validarDadosUsuario(View view){
        String nome = editUsuarioNome.getText().toString();
        String endereco = editUsuarioEndereco.getText().toString();

        if (!nome.isEmpty()){
            if (!endereco.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNome(nome);
                usuario.setEndereco(endereco);
                usuario.salvar();
                exibirMensagem("Dados salvos com sucesso!");
                finish();
            }else{
             exibirMensagem("Digite seu endereço completo!");
            }
        }else{
            exibirMensagem("Digite seu nome!");
        }
    }

    private void recuperarDadosUsuario(){
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    editUsuarioNome.setText(usuario.getNome());
                    editUsuarioEndereco.setText(usuario.getEndereco());
                    //editUsuarioTelefone.setText(usuario.getTelefone());

                    /*
                    urlImagemSelecionada = usuario.getUrlImagem();
                    if (urlImagemSelecionada != ""){
                        Picasso.get().load(urlImagemSelecionada).into(imagePerfilEmpresa);
                    }

                     */
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes(){
        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioEndereco = findViewById(R.id.editUsuarioEndereco);
    }
}
