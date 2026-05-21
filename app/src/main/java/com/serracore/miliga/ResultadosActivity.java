package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultadosActivity extends MenuActivity {

    private ListView listResultados;
    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter;

    private String idLiga;

    private ArrayList<String> idsPartidos = new ArrayList<>();

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

        // ✅ CLICK → VER DETALLE
        listResultados.setOnItemClickListener((parent, view, position, id) -> {

            if (position >= idsPartidos.size()) return;

            String idPartido = idsPartidos.get(position);

            if (idPartido == null || idPartido.isEmpty()) return;

            Intent intent = new Intent(this, DetallePartidoActivity.class);
            intent.putExtra("idLiga", idLiga);
            intent.putExtra("idPartido", idPartido);

            startActivity(intent);
        });

        // ✅ PULSACIÓN LARGA → ELIMINAR
        listResultados.setOnItemLongClickListener((parent, view, position, id) -> {

            if (position >= idsPartidos.size()) return true;

            String idPartido = idsPartidos.get(position);

            if (idPartido == null || idPartido.isEmpty()) return true;

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar partido")
                    .setMessage("¿Seguro que quieres eliminar este partido?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        FirebaseDatabase.getInstance()
                                .getReference("ligas")
                                .child(idLiga)
                                .child("partidos")
                                .child(idPartido)
                                .removeValue();

                        Toast.makeText(this,
                                "🗑️ Partido eliminado",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
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

                        // ✅ Construir lista
                        for (String jornada : ordenadas) {

                            lista.add("📅 Jornada " + jornada);
                            idsPartidos.add(""); // no clicable

                            ArrayList<String> listaPartidos = jornadas.get(jornada);
                            ArrayList<String> listaIds = jornadasIds.get(jornada);

                            for (int i = 0; i < listaPartidos.size(); i++) {

                                lista.add(listaPartidos.get(i));
                                idsPartidos.add(listaIds.get(i));
                            }

                            lista.add("");
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