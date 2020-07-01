package com.example.bomapetite.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bomapetite.R;
import com.example.bomapetite.helper.ConfiguracaoFirebase;
import com.example.bomapetite.helper.UsuarioFirebase;
import com.example.bomapetite.model.Empresa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private EditText editEmpresaNome, editEmpresaCategoria, editEmpresaTempo, editEmpresaTaxa;
    private ImageView imagePerfilEmpresa;

    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private String urlImagemSelecionada = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);
        getSupportActionBar().setTitle("Configurações");

        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        imagePerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        recuperarDadosEmpresa();

    }

    private void recuperarDadosEmpresa(){
        DatabaseReference empresaRef = firebaseRef.child("empresas").child(idUsuarioLogado);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    Empresa empresa = dataSnapshot.getValue(Empresa.class);
                    editEmpresaNome.setText(empresa.getNome());
                    editEmpresaCategoria.setText(empresa.getCategoria());
                    editEmpresaTempo.setText(empresa.getTempo());
                    editEmpresaTaxa.setText(empresa.getPrecoEntrega().toString());

                    urlImagemSelecionada = empresa.getUrlImagem();
                    if (urlImagemSelecionada != ""){
                        Picasso.get().load(urlImagemSelecionada).into(imagePerfilEmpresa);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void validarDadosEmpresa(View view){
        String nome = editEmpresaNome.getText().toString();
        String nome_filtro = editEmpresaNome.getText().toString();
        String taxa = editEmpresaTaxa.getText().toString();
        String tempo = editEmpresaTempo.getText().toString();
        String categoria = editEmpresaCategoria.getText().toString();

        if (!nome.isEmpty()){
            if (!categoria.isEmpty()){
                if (!tempo.isEmpty()){
                    if (!taxa.isEmpty()){
                        Empresa empresa = new Empresa();
                        empresa.setIdUsuario(idUsuarioLogado);
                        empresa.setNome(nome);
                        empresa.setNome_filtro(nome_filtro);
                        empresa.setPrecoEntrega(Double.parseDouble(taxa));
                        empresa.setCategoria(categoria);
                        empresa.setTempo(tempo);
                        empresa.setUrlImagem(urlImagemSelecionada);
                        empresa.salvar();
                        finish();
                    }else{
                        exibirMensagem("Digite uma taxa de entrega");
                    }
                }else{
                    exibirMensagem("Digite um tempo de entrega");
                }
            }else{
                exibirMensagem("Digite uma categoria");
            }
        }else{
            exibirMensagem("Digite um nome para a empresa");
        }
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {

                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagem);
                        break;
                }

                if (imagem != null){
                    imagePerfilEmpresa.setImageBitmap(imagem);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    StorageReference imagemRef = storageReference.child("imagens").child("empresas").child(idUsuarioLogado + "jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesEmpresaActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            urlImagemSelecionada = taskSnapshot.getDownloadUrl().toString();
                            Toast.makeText(ConfiguracoesEmpresaActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void inicializarComponentes(){
        editEmpresaNome = findViewById(R.id.editEmpresaNome);
        editEmpresaCategoria = findViewById(R.id.editEmpresaCategoria);
        editEmpresaTempo = findViewById(R.id.editEmpresaTempo);
        editEmpresaTaxa = findViewById(R.id.editEmpresaValorEntrega);
        imagePerfilEmpresa = findViewById(R.id.imagePerfilEmpresa);

    }
}
