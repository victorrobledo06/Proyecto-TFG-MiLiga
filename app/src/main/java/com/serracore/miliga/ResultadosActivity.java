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

// Activity encargada de mostrar los resultados de los partidos agrupados por jornada.
// Permite además ver el detalle de cada partido y eliminarlo mediante pulsación larga.
public class ResultadosActivity extends MenuActivity {

    private ListView listResultados;

    // Lista que contiene los textos que se muestran en pantalla
    private ArrayList<String> lista;

    private ArrayAdapter<String> adapter;

    private String idLiga;

    // Lista paralela que guarda los IDs de los partidos en Firebase
    private ArrayList<String> idsPartidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        setTitle("Resultados - MiLiga");

        // Referencia al ListView
        listResultados = findViewById(R.id.listResultados);

        // Inicialización de la lista y adaptador
        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        listResultados.setAdapter(adapter);

        // Obtener ID de la liga
        idLiga = getIntent().getStringExtra("idLiga");

        // Cargar partidos
        cargarPartidos();

        // CLICK → VER DETALLE DEL PARTIDO
        listResultados.setOnItemClickListener((parent, view, position, id) -> {

            // Evitar errores en posiciones no válidas (títulos o separadores)
            if (position >= idsPartidos.size()) return;

            String idPartido = idsPartidos.get(position);

            if (idPartido == null || idPartido.isEmpty()) return;

            // Ir a la pantalla de detalle de partido
            Intent intent = new Intent(this, DetallePartidoActivity.class);
            intent.putExtra("idLiga", idLiga);
            intent.putExtra("idPartido", idPartido);

            startActivity(intent);
        });

        // PULSACIÓN LARGA → ELIMINAR PARTIDO
        listResultados.setOnItemLongClickListener((parent, view, position, id) -> {

            // Evitar borrar elementos no válidos
            if (position >= idsPartidos.size()) return true;

            String idPartido = idsPartidos.get(position);

            if (idPartido == null || idPartido.isEmpty()) return true;

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar partido")
                    .setMessage("¿Seguro que quieres eliminar este partido?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        // Eliminar partido de Firebase
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

    // Método que carga los partidos desde Firebase y los organiza por jornadas
    private void cargarPartidos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Limpiar listas
                        lista.clear();
                        idsPartidos.clear();

                        // Mapas para agrupar partidos por jornada
                        HashMap<String, ArrayList<String>> jornadas = new HashMap<>();
                        HashMap<String, ArrayList<String>> jornadasIds = new HashMap<>();

                        // Recorrer todos los partidos
                        for (DataSnapshot partido : snapshot.getChildren()) {

                            String jornada = partido.child("jornada").getValue(String.class);

                            if (jornada == null) jornada = "0";

                            String idPartido = partido.getKey();

                            String local = partido.child("equipoLocal").getValue(String.class);
                            String visitante = partido.child("equipoVisitante").getValue(String.class);

                            String gl = partido.child("golesLocal").getValue(String.class);
                            String gv = partido.child("golesVisitante").getValue(String.class);

                            // Crear texto del resultado
                            String texto = local + " " + gl + " - " + gv + " " + visitante;

                            // Crear estructuras si no existen
                            if (!jornadas.containsKey(jornada)) {
                                jornadas.put(jornada, new ArrayList<>());
                                jornadasIds.put(jornada, new ArrayList<>());
                            }

                            jornadas.get(jornada).add(texto);
                            jornadasIds.get(jornada).add(idPartido);
                        }

                        // Ordenar las jornadas
                        ArrayList<String> ordenadas = new ArrayList<>(jornadas.keySet());
                        Collections.sort(ordenadas, (a, b) -> {
                            if (a == null) return 1;
                            if (b == null) return -1;
                            return a.compareTo(b);
                        });

                        // Construcción de la lista final a mostrar
                        for (String jornada : ordenadas) {

                            // Título de la jornada
                            lista.add("📅 Jornada " + jornada);
                            idsPartidos.add(""); // no clicable

                            ArrayList<String> listaPartidos = jornadas.get(jornada);
                            ArrayList<String> listaIds = jornadasIds.get(jornada);

                            // Añadir partidos de esa jornada
                            for (int i = 0; i < listaPartidos.size(); i++) {

                                lista.add(listaPartidos.get(i));
                                idsPartidos.add(listaIds.get(i));
                            }

                            // Separador visual
                            lista.add("");
                            idsPartidos.add("");
                        }

                        // Si no hay partidos
                        if (lista.isEmpty()) {
                            lista.add("No hay resultados disponibles");
                        }

                        // Actualizar la vista
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
