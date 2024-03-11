package com.example.patrimoniocontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "patrimonio.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "moveis";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_LOCAL = "local";
    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NOME + " TEXT, " +
                        COLUMN_DESCRICAO + " TEXT, " +
                        COLUMN_LOCAL + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addMovel(String nome, String descricao, String local){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NOME, nome);
        contentValues.put(COLUMN_DESCRICAO, descricao);
        contentValues.put(COLUMN_LOCAL, local);
        long resultado = db.insert(TABLE_NAME, null, contentValues);
        if (resultado == -1){
            Toast.makeText(context, "Falha ao adicionar movel!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Móvel adicionado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = " SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    void update(String row_id, String nome, String descricao, String local){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOME, nome);
        contentValues.put(COLUMN_DESCRICAO, descricao);
        contentValues.put(COLUMN_LOCAL, local);

      long resultado =  db.update(TABLE_NAME, contentValues, "id=?", new String[]{row_id});
      if(resultado == -1){
          Toast.makeText(context, "erro ao tentar atualizar!", Toast.LENGTH_SHORT).show();
      } else {
          Toast.makeText(context, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
      }
    }

    void deletar(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long resultado = db.delete(TABLE_NAME, "id=?", new String[]{row_id});
        if(resultado == -1){
            Toast.makeText(context,"falha ao apagar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Apagado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }
}