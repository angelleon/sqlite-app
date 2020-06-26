package com.pandalab.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CarroAdapter extends ArrayAdapter <Carro> {
    CarroAdapter(Context context, int resource, ArrayList<Carro> carros) {

        super(context, resource, carros);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Carro carro = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.carro_item, parent, false);
        }
        TextView txtSerie = (TextView) convertView.findViewById(R.id.txtItemSerie);
        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtItemNombre);
        TextView txtColor = (TextView) convertView.findViewById(R.id.txtItemColor);
        txtNombre.setText(carro.getNombre());
        txtColor.setText(carro.getColor());
        int serie = carro.getSerie();
        txtSerie.setText("" + serie);
        return convertView;
    }
}
