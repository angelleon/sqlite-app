package com.pandalab.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Carro> datos;
    private CarroAdapter datosAdapter;
    private int selectedId;
    private int selectedIndex;
    private EditText txtSerie;
    private EditText txtNombre;
    private EditText txtColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnInsertar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCarro();
            }
        });
        findViewById(R.id.btnBorrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCarro();
            }
        });
        findViewById(R.id.btnLimpiar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetIndex();
                cleanInput();
            }
        });
        findViewById(R.id.btnSeleccionar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarCarro();
            }
        });
        selectedId = -1;
        selectedIndex = -1;
        txtSerie = findViewById(R.id.txtSerie);
        txtNombre = findViewById(R.id.txtNombre);
        txtColor = findViewById(R.id.txtColor);

        MainActivity.TextWatcher txtListener = new MainActivity.TextWatcher();
        txtSerie.addTextChangedListener(txtListener);
        txtNombre.addTextChangedListener(txtListener);
        txtColor.addTextChangedListener(txtListener);
        ListView lista = findViewById(R.id.lstCarros);
        datos = new ArrayList<>();
        datosAdapter = new CarroAdapter(this, R.layout.carro_item, datos);
        lista.setAdapter(datosAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Carro selected = datos.get(position);
                ((TextView) findViewById(R.id.txtSerie)).setText(String.valueOf(selected.getSerie()));
                ((TextView) findViewById(R.id.txtNombre)).setText(selected.getNombre());
                ((TextView) findViewById(R.id.txtColor)).setText(selected.getColor());
                selectedIndex = position;
                selectedId = selected.getId();
            }
        });
        seleccionarCarro();
    }

    private void crearCarro() {
        CarroSQLiteHelper conn = new CarroSQLiteHelper(this, "db_carros", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        ContentValues carro = new ContentValues();
        EditText txtSerie = findViewById(R.id.txtSerie);
        EditText txtNombre = findViewById(R.id.txtNombre);
        EditText txtColor = findViewById(R.id.txtColor);

        int serie = Integer.parseInt(txtSerie.getText().toString());
        String nombre = txtNombre.getText().toString();
        String color = txtColor.getText().toString();
        carro.put(Carro.SERIE, serie);
        carro.put(Carro.NOMBRE, nombre);
        carro.put(Carro.COLOR, color);
        long id = db.insert(Carro.TABLENAME, Carro.SERIE, carro);
        datos.add(new Carro((int) id, serie, nombre, color));
        //datosAdapter.add(new Carro(serie, nombre, color));
        datosAdapter.notifyDataSetChanged();

        db.close();
        conn.close();

    }

    private void seleccionarCarro() {
        String whereClause = "";
        List<String> whereArgsList = new ArrayList<>();
        if (txtSerie.length() > 0) {
            whereClause += " serie = ? ";
            whereArgsList.add(txtSerie.getText().toString());
        }
        if (txtNombre.length() > 0) {
            if (whereClause.length() > 0) {
                whereClause += " AND ";
            }
            whereClause += " nombre = ? ";
            whereArgsList.add(txtNombre.getText().toString());
        }
        if (txtColor.length() > 0) {
            if (whereClause.length() > 0) {
                whereClause += " AND ";
            }
            whereClause += " color = ? ";
            whereArgsList.add(txtColor.getText().toString());
        }
        Log.d("dbg", "Selecting WHERE " + whereClause);
        for (Object o: whereArgsList.toArray()) {
            Log.d("dbg", String.valueOf(o));
        }
        String[] whereArgs = null;
        if (whereClause.length() > 0) {
            whereArgs = new String[whereArgsList.size()];
            whereArgsList.toArray(whereArgs);
        } else {
            whereClause = null;
        }
        CarroSQLiteHelper conn = new CarroSQLiteHelper(this, "db_carros", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cur = db.query("Carro", new String[] {"id", "serie", "nombre", "color"}, whereClause, whereArgs, null, null, null);
        if (cur.getCount() > 0) {
            datosAdapter.clear();
            cur.moveToFirst();
            while (!cur.isLast()) {
                int idIndex = cur.getColumnIndex("id");
                int id = cur.getInt(idIndex);
                int serieIndex = cur.getColumnIndex("serie");
                int serie = cur.getInt(serieIndex);
                int nombreIndex = cur.getColumnIndex("nombre");
                String nombre = cur.getString(nombreIndex);
                int colorIndex = cur.getColumnIndex("color");
                String color = cur.getString(colorIndex);
                datosAdapter.add(new Carro(id, serie, nombre, color));
                cur.moveToNext();
            }
        }
        cur.close();
        datosAdapter.notifyDataSetChanged();
    }

    private void cleanInput() {
        ((EditText) findViewById(R.id.txtSerie)).setText("");
        ((EditText) findViewById(R.id.txtNombre)).setText("");
        ((EditText) findViewById(R.id.txtColor)).setText("");
    }

    private void eliminarCarro() {
        Log.d("dbg", "Removing element");
        if (selectedIndex != -1) {
            CarroSQLiteHelper conn = new CarroSQLiteHelper(this, "db_carros", null, 1);
            SQLiteDatabase db = conn.getWritableDatabase();
            if (db.delete("Carro", " id = ? ", new String[] {String.valueOf(selectedId)}) == 1) {
                datosAdapter.remove(datos.get(selectedIndex));
                resetIndex();
                cleanInput();
            } else {
                Log.d("dbg", String.format("Element not deleted id: [%d], index[%d]", selectedId, selectedIndex));
            }
            db.close();
            conn.close();
        } else
            Log.d("dbg", "No element select, so not removed");
    }

    private void resetIndex() {
        Log.d("dbg", "indexes reseted");
        selectedId = -1;
        selectedIndex = -1;
    }

    class TextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            resetIndex();
        }
    }
}
