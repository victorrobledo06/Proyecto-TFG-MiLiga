package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EstadisticasActivity extends MenuActivity {

    private ListView listStats;
    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter;

    private String idLiga;

    private HashMap<String, Integer> golesMap = new HashMap<>();
    private HashMap<String, Integer> asistenciasMap = new HashMap<>();
    private HashMap<String, Integer> amarillasMap = new HashMap<>();
    private HashMap<String, Integer> rojasMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        listStats = findViewById(R.id.listStats);

        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        listStats.setAdapter(adapter);

        idLiga = getIntent().getStringExtra("idLiga");

        cargarDatos();
    }

    private void cargarDatos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot partido : snapshot.getChildren()) {

                            contar(partido, "goles", golesMap);
                            contar(partido, "asistencias", asistenciasMap);
                            contar(partido, "amarillas", amarillasMap);
                            contar(partido, "rojas", rojasMap);
                        }

                        mostrar();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void contar(DataSnapshot partido, String campo, HashMap<String, Integer> map) {

        if (!partido.hasChild(campo)) return;

        for (DataSnapshot item : partido.child(campo).getChildren()) {

            String jugador = item.getValue(String.class);

            map.put(jugador, map.getOrDefault(jugador, 0) + 1);
        }
    }

    private void mostrar() {

        lista.clear();

        lista.add("⚽ GOLEADORES");
        mostrarTop(golesMap);

        lista.add("\n🎯 ASISTENCIAS");
        mostrarTop(asistenciasMap);

        lista.add("\n🟨 AMARILLAS");
        mostrarTop(amarillasMap);

        lista.add("\n🟥 ROJAS");
        mostrarTop(rojasMap);

        adapter.notifyDataSetChanged();
    }

    private void mostrarTop(HashMap<String, Integer> map) {

        map.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> {

                    lista.add(entry.getKey() + " - " + entry.getValue());
                });
    }
}
