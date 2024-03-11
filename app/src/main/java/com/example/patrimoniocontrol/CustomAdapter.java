package com.example.patrimoniocontrol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MeyViewHolder> {

   private Context context;

   Activity activity;
   private ArrayList id, nome, descricao, local;

    CustomAdapter(Activity activity,
                  Context context,
                  ArrayList id,
                  ArrayList nome,
                  ArrayList descricao,
                  ArrayList local){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
    }

    @NonNull
    @Override
    public CustomAdapter.MeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent, false);
        return new MeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MeyViewHolder holder, int position) {
        holder.id.setText(String.valueOf(position + 1));
        holder.nome.setText(String.valueOf(nome.get(position)));
        holder.local.setText(String.valueOf(local.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("nome", String.valueOf(nome.get(position)));
                intent.putExtra("descricao", String.valueOf(descricao.get(position)));
                intent.putExtra("local", String.valueOf(local.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nome.size();
    }

    public class MeyViewHolder extends RecyclerView.ViewHolder {
        TextView id, nome,  descricao, local;
        LinearLayout mainLayout;
        public MeyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.movel_id_txt);
            nome = itemView.findViewById(R.id.nome_txt);
            local = itemView.findViewById(R.id.local_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}