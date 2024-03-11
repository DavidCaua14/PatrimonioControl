package com.example.patrimoniocontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton btn_adicionar;
    MyDatabaseHelper myDatabaseHelper;
    ArrayList<String> id, nome, descricao, local;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btn_adicionar = findViewById(R.id.btn_adicionar);

        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        myDatabaseHelper = new MyDatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        nome = new ArrayList<>();
        descricao = new ArrayList<>();
        local = new ArrayList<>();

        storeDataInArrays();
        customAdapter = new CustomAdapter(MainActivity.this, this, id, nome, descricao, local);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDatabaseHelper.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Não existe Móvel cadastrado!", Toast.LENGTH_SHORT).show();
        } else{
            int position = 1;
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                nome.add(cursor.getString(1));
                descricao.add(cursor.getString(2));
                local.add(cursor.getString(3));
                position++;
            }
        }
    }
}