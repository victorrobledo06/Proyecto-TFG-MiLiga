package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultadosActivity extends MenuActivity {

    private ListView listResultados;
    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter;

    private String idLiga;

    private ArrayList<String> idsPartidos = new ArrayList<>(); // ✅ ids reales

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        setTitle("Resultados - MiLiga");

        listResultados = findViewById(R.id.listResultados);

        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        listResultados.setAdapter(adapter);

        idLiga = getIntent().getStringExtra("idLiga");

        cargarPartidos();

        // ✅ CLICK PARA VER DETALLE
        listResultados.setOnItemClickListener((parent, view, position, id) -> {

            // ⚠️ Evitar clic en títulos o líneas vacías
            if (position >= idsPartidos.size()) return;

            Intent intent = new Intent(this, DetallePartidoActivity.class);
            intent.putExtra("idLiga", idLiga);
            intent.putExtra("idPartido", idsPartidos.get(position));

            startActivity(intent);
        });
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
                        idsPartidos.clear();

                        HashMap<String, ArrayList<String>> jornadas = new HashMap<>();
                        HashMap<String, ArrayList<String>> jornadasIds = new HashMap<>();

                        for (DataSnapshot partido : snapshot.getChildren()) {

                            String jornada = partido.child("jornada").getValue(String.class);

                            if (jornada == null) jornada = "0";

                            String idPartido = partido.getKey();

                            String local = partido.child("equipoLocal").getValue(String.class);
                            String visitante = partido.child("equipoVisitante").getValue(String.class);

                            String gl = partido.child("golesLocal").getValue(String.class);
                            String gv = partido.child("golesVisitante").getValue(String.class);

                            String texto = local + " " + gl + " - " + gv + " " + visitante;

                            if (!jornadas.containsKey(jornada)) {
                                jornadas.put(jornada, new ArrayList<>());
                                jornadasIds.put(jornada, new ArrayList<>());
                            }

                            jornadas.get(jornada).add(texto);
                            jornadasIds.get(jornada).add(idPartido);
                        }

                        // ✅ Ordenar jornadas
                        ArrayList<String> ordenadas = new ArrayList<>(jornadas.keySet());
                        Collections.sort(ordenadas, (a, b) -> {
                            if (a == null) return 1;
                            if (b == null) return -1;
                            return a.compareTo(b);
                        });

                        // ✅ Construir lista final
                        for (String jornada : ordenadas) {

                            lista.add("📅 Jornada " + jornada);
                            idsPartidos.add(""); // placeholder (no clicable)

                            ArrayList<String> listaPartidos = jornadas.get(jornada);
                            ArrayList<String> listaIds = jornadasIds.get(jornada);

                            for (int i = 0; i < listaPartidos.size(); i++) {

                                lista.add(listaPartidos.get(i));
                                idsPartidos.add(listaIds.get(i));
                            }

                            lista.add(""); // espacio visual
                            idsPartidos.add(""); // placeholder
                        }

                        if (lista.isEmpty()) {
                            lista.add("No hay resultados disponibles");
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}