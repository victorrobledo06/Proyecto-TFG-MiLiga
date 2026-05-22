
package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

// Activity encargada de calcular y mostrar estadísticas de jugadores
// (goles, asistencias, tarjetas amarillas y rojas)
public class EstadisticasActivity extends MenuActivity {

    private ListView listStats;

    // Lista que se muestra en pantalla
    private ArrayList<String> lista;

    private ArrayAdapter<String> adapter;

    private String idLiga;

    // Mapas para contar estadísticas por jugador
    private HashMap<String, Integer> golesMap = new HashMap<>();
    private HashMap<String, Integer> asistenciasMap = new HashMap<>();
    private HashMap<String, Integer> amarillasMap = new HashMap<>();
    private HashMap<String, Integer> rojasMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        // Referencia al ListView
        listStats = findViewById(R.id.listStats);

        lista = new ArrayList<>();

        // Adaptador para mostrar estadísticas en la lista
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        listStats.setAdapter(adapter);

        // Obtener ID de la liga desde la activity anterior
        idLiga = getIntent().getStringExtra("idLiga");

        // Cargar datos desde Firebase
        cargarDatos();
    }

    // Método que obtiene los partidos y procesa estadísticas
    private void cargarDatos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Recorrer todos los partidos
                        for (DataSnapshot partido : snapshot.getChildren()) {

                            // Contar eventos de cada tipo
                            contar(partido, "goles", golesMap);
                            contar(partido, "asistencias", asistenciasMap);
                            contar(partido, "amarillas", amarillasMap);
                            contar(partido, "rojas", rojasMap);
                        }

                        // Mostrar resultados
                        mostrar();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    // Método genérico que cuenta eventos de un tipo determinado
    private void contar(DataSnapshot partido, String campo, HashMap<String, Integer> map) {

        // Si no existe ese tipo de evento, salir
        if (!partido.hasChild(campo)) return;

        // Recorrer cada evento
        for (DataSnapshot item : partido.child(campo).getChildren()) {

            String jugadorCompleto = item.getValue(String.class);

            // Extraer solo el nombre del jugador (sin minuto ni otros datos)
            String jugador = jugadorCompleto;

            if (jugadorCompleto.contains("(")) {
                jugador = jugadorCompleto.substring(0, jugadorCompleto.indexOf("(")).trim();
            }

            // Incrementar contador del jugador
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


    // Método que ordena y muestra un ranking de jugadores
    private void mostrarTop(HashMap<String, Integer> map) {

        map.entrySet().stream()
                // Ordenar de mayor a menor
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(entry -> {

                    // Mostrar jugador y número de eventos
                    lista.add(entry.getKey() + " - " + entry.getValue());
                });
    }
}

