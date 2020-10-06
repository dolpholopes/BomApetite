package com.example.bomapetite.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bomapetite.R;
import com.example.bomapetite.adapter.AdapterProduto;
import com.example.bomapetite.helper.ConfiguracaoFirebase;
import com.example.bomapetite.helper.UsuarioFirebase;
import com.example.bomapetite.model.Empresa;
import com.example.bomapetite.model.ItemPedido;
import com.example.bomapetite.model.Pedido;
import com.example.bomapetite.model.Produto;
import com.example.bomapetite.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutosCarrinho;
    private ImageView imageEmpresaCardapio;
    private TextView textNomeEmpresaCardapio, textIrCarrinho;
    private Empresa empresaSelecionada;
    private AlertDialog dialog;
    private TextView textCarrinhoQtd, textCarrinhoTotal;

    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idEmpresa;
    private String idUsuarioLogado;
    private Usuario usuario;
    private Pedido pedidoRecuperado;
    private int qtdItensCarrinho;
    private Double totalCarrinho;
    private int metodoPagamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        getSupportActionBar().setTitle("Seu carrinho");

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();


        //Recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");

            textNomeEmpresaCardapio.setText( empresaSelecionada.getNome() );
            idEmpresa = empresaSelecionada.getIdUsuario();

            String url = empresaSelecionada.getUrlImagem();
            Picasso.get().load(url).into(imageEmpresaCardapio);

        }

        recyclerProdutosCarrinho.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutosCarrinho.setHasFixedSize(true);
        recyclerProdutosCarrinho.setAdapter(adapterProduto);


        recuperarDadosUsuario();

    }

    private void inicializarComponentes(){
        recyclerProdutosCarrinho = findViewById(R.id.recyclerProdutosCardapio);

    }


    private void recuperarDadosUsuario() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable( false )
                .build();
        dialog.show();

        DatabaseReference usuariosRef = firebaseRef
                .child("usuarios")
                .child( idUsuarioLogado );

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null ){
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperPedido();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void recuperPedido() {

        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child( idEmpresa )
                .child( idUsuarioLogado );

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                itensCarrinho = new ArrayList<>();

                if(dataSnapshot.getValue() != null){

                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);
                    itensCarrinho = pedidoRecuperado.getItens();


                    for(ItemPedido itemPedido: itensCarrinho){

                        int qtde = itemPedido.getQuantidade();
                        Double preco = itemPedido.getPreco();

                        totalCarrinho += (qtde * preco);
                        qtdItensCarrinho += qtde;

                    }

                }

                DecimalFormat df = new DecimalFormat("0.00");

               // textCarrinhoQtd.setText( "qtd: " + String.valueOf(qtdItensCarrinho) );
               // textCarrinhoTotal.setText("R$ " + df.format( totalCarrinho ) );

                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}