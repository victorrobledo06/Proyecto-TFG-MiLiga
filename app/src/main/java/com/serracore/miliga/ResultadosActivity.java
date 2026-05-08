package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultadosActivity extends MenuActivity {

    private ListView listResultados;
    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter;

    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        listResultados = findViewById(R.id.listResultados);

        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        listResultados.setAdapter(adapter);

        idLiga = getIntent().getStringExtra("idLiga");

        cargarPartidos();
    }

    private void cargarPartidos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        lista.clear();

                        // Agrupar por jornada
                        HashMap<String, ArrayList<String>> jornadas = new HashMap<>();

                        for (DataSnapshot partido : snapshot.getChildren()) {

                            String jornada = partido.child("jornada").getValue(String.class);

                            String local = partido.child("equipoLocal").getValue(String.class);
                            String visitante = partido.child("equipoVisitante").getValue(String.class);

                            String gl = partido.child("golesLocal").getValue(String.class);
                            String gv = partido.child("golesVisitante").getValue(String.class);

                            String texto = local + " " + gl + " - " + gv + " " + visitante;

                            if (!jornadas.containsKey(jornada)) {
                                jornadas.put(jornada, new ArrayList<>());
                            }

                            jornadas.get(jornada).add(texto);
                        }

                        // Mostrar
                        for (String jornada : jornadas.keySet()) {

                            lista.add("📅 Jornada " + jornada);

                            for (String partido : jornadas.get(jornada)) {
                                lista.add(partido);
                            }

                            lista.add(""); // espacio
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
