package com.example.patrimoniocontrol;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText campo_nome, campo_descricao, campo_local;
    Button btn_atualizar, btn_deletar;

    String id, nome, descricao, local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        campo_nome = findViewById(R.id.campo_nome2);
        campo_descricao = findViewById(R.id.campo_descrição2);
        campo_local = findViewById(R.id.campo_local2);
        btn_atualizar = findViewById(R.id.btn_atualizar);
        btn_deletar = findViewById(R.id.btn_deletar);
        getAndSetIntentData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(nome);
        }

        btn_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(UpdateActivity.this);
                myDatabaseHelper.update(id, campo_nome.getText().toString().trim(),
                        campo_descricao.getText().toString().trim(), campo_local.getText().toString().trim());
                finish();
            }
        });
        btn_deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("nome") &&
                getIntent().hasExtra("descricao") && getIntent().hasExtra("local")){
            id = getIntent().getStringExtra("id");
            nome = getIntent().getStringExtra("nome");
            descricao = getIntent().getStringExtra("descricao");
            local = getIntent().getStringExtra("local");

            campo_nome.setText(nome);
            campo_descricao.setText(descricao);
            campo_local.setText(local);
        } else {
            Toast.makeText(this, "Não existe dados", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Você realmente deseja deletar?");
        builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(UpdateActivity.this);
                myDatabaseHelper.deletar(id);
                finish();
            }
        });
        builder.setNegativeButton("não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}