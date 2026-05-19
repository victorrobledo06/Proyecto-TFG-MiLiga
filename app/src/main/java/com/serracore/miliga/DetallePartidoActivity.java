package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class DetallePartidoActivity extends MenuActivity {

    private TextView tvTitulo;
    private ListView listEventos;

    private ArrayList<String> eventos;
    private ArrayAdapter<String> adapter;

    private String idLiga, idPartido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_partido);

        tvTitulo = findViewById(R.id.tvTituloPartido);
        listEventos = findViewById(R.id.listEventos);

        eventos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventos);

        listEventos.setAdapter(adapter);

        idLiga = getIntent().getStringExtra("idLiga");
        idPartido = getIntent().getStringExtra("idPartido");

        cargarPartido();
    }

    private void cargarPartido() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .child(idPartido)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data) {

                        String local = data.child("equipoLocal").getValue(String.class);
                        String visitante = data.child("equipoVisitante").getValue(String.class);

                        String gl = data.child("golesLocal").getValue(String.class);
                        String gv = data.child("golesVisitante").getValue(String.class);

                        tvTitulo.setText(local + " " + gl + " - " + gv + " " + visitante);

                        eventos.clear();

                        añadirEventos(data, "goles", "⚽ ");
                        añadirEventos(data, "asistencias", "🎯 ");
                        añadirEventos(data, "amarillas", "🟨 ");
                        añadirEventos(data, "rojas", "🟥 ");

                        if (eventos.isEmpty()) {
                            eventos.add("No hay eventos en este partido");
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void añadirEventos(DataSnapshot data, String campo, String icono) {

        if (!data.hasChild(campo)) return;

        for (DataSnapshot item : data.child(campo).getChildren()) {

            String evento = item.getValue(String.class);

            eventos.add(icono + evento);
        }
    }
}
