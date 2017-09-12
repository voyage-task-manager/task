package com.example.felipe.app;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import models.Task;

/**
 * Created by felipe on 02/09/17.
 */

public class PersonalAdapter extends BaseAdapter {

    private final List<Task> tasks;
    private final Activity act;

    public PersonalAdapter(Activity act, List<Task> tasks) {
        this.tasks = tasks;
        this.act = act;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.lista_personalizada, parent, false);


        Task task = tasks.get(position);

        TextView nome = (TextView)
        view.findViewById(R.id.lista_personalizada_nome);
        //TextView descricao = (TextView) view.findViewById(R.id.lista_personalizada_descricao);
        //ImageView imagem = (ImageView) view.findViewById(R.id.lista_personalizada_imagem);

        nome.setText(task.getTitle());
        //descricao.setText(curso.getDescricao());
        //imagem.setImageResource(R.drawable.java);

        return view;
    }
}
