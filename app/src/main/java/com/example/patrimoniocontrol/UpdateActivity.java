package com.example.patrimoniocontrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    EditText campo_nome, campo_descricao, campo_local;
    Button btn_atualizar, btn_deletar, btn_atualizar_foto;
    String id, nome, descricao, local;
    String pathToFile;
    String caminhoFoto = pathToFile;
    ImageView imageView;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        campo_nome = findViewById(R.id.campo_nome2);
        campo_descricao = findViewById(R.id.campo_descrição2);
        campo_local = findViewById(R.id.campo_local2);
        btn_atualizar = findViewById(R.id.btn_atualizar);
        btn_deletar = findViewById(R.id.btn_deletar);
        btn_atualizar_foto = findViewById(R.id.btn_atualizar_foto);
        imageView = findViewById(R.id.imagemDoBanco);
        myDatabaseHelper = new MyDatabaseHelper(this);

        getAndSetIntentData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(nome);
        }

        btn_atualizar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateActivity.this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(UpdateActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                    dispatchPictureTakeAction();
                } else {
                    ActivityCompat.requestPermissions(UpdateActivity.this,
                            new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });

        imageView = findViewById(R.id.imagemDoBanco);
        btn_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabaseHelper.update(id, campo_nome.getText().toString().trim(),
                        campo_descricao.getText().toString().trim(), campo_local.getText().toString().trim(), caminhoFoto);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchPictureTakeAction();
            } else {
                dispatchPictureTakeAction();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // Carregar a imagem capturada no ImageView
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
                // Definir o caminho da foto
                caminhoFoto = pathToFile;
            }
        }
    }
    public void dispatchPictureTakeAction(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile = createPhotoFile();
            if (photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(UpdateActivity.this,
                        "com.example.android.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }
        }
    }
    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("log", "Excep : " + e.toString());
        }
        return image;
    }
    @SuppressLint("Range")
    private void getAndSetIntentData() {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("nome") &&
                getIntent().hasExtra("descricao") && getIntent().hasExtra("local")) {
            id = getIntent().getStringExtra("id");
            nome = getIntent().getStringExtra("nome");
            descricao = getIntent().getStringExtra("descricao");
            local = getIntent().getStringExtra("local");
            campo_nome.setText(nome);
            campo_descricao.setText(descricao);
            campo_local.setText(local);
            Cursor cursor = myDatabaseHelper.getDataById(id);
            if (cursor != null && cursor.moveToFirst()) {
                caminhoFoto = cursor.getString(cursor.getColumnIndex("foto"));
                if (caminhoFoto != null) {
                    // Carregar a imagem no ImageView
                    imageView.setImageURI(Uri.parse(caminhoFoto));
                }
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Não existem dados", Toast.LENGTH_SHORT).show();
        }
    }
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Você realmente deseja deletar?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDatabaseHelper.deletar(id);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar a exclusão
            }
        });
        builder.create().show();
    }
}