package com.example.patrimoniocontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {
    EditText campo_nome, campo_descricao, campo_local;
    Button btn_adicionar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        campo_nome = findViewById(R.id.campo_nome);
        campo_descricao = findViewById(R.id.campo_descrição);
        campo_local = findViewById(R.id.campo_local);
        btn_adicionar = findViewById(R.id.btn_adicionar);
        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(AddActivity.this);
                myDatabaseHelper.addMovel(campo_nome.getText().toString().trim(),
                        campo_descricao.getText().toString().trim(),
                        campo_local.getText().toString().trim());
            }
        });
    }
}