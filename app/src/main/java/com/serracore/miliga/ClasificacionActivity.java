package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

// Activity encargada de calcular y mostrar la clasificación de la liga
public class ClasificacionActivity extends MenuActivity {

    private ListView listClasificacion;

    // Lista que se mostrará en pantalla (texto final con clasificación)
    private ArrayList<String> tabla;

    private ArrayAdapter<String> adapter;

    // Mapa donde guardamos estadísticas de cada equipo
    private HashMap<String, EquipoStats> statsMap;

    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);

        // Obtener referencia al ListView
        listClasificacion = findViewById(R.id.listClasificacion);

        tabla = new ArrayList<>();

        // Adaptador para mostrar los datos en la lista
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, tabla);

        listClasificacion.setAdapter(adapter);

        // Inicializar el mapa de estadísticas
        statsMap = new HashMap<>();

        //  Obtener ID de la liga desde la activity anterior
        idLiga = getIntent().getStringExtra("idLiga");

        // Comenzamos cargando los equipos
        cargarEquipos();
    }

    // Método para cargar todos los equipos de la liga
    private void cargarEquipos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Recorrer cada equipo
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);

                            // Crear objeto de estadísticas para cada equipo
                            statsMap.put(nombre, new EquipoStats(nombre));
                        }

                        // Una vez cargados los equipos, procesamos los partidos
                        cargarPartidos();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    // Método para leer los partidos y calcular estadísticas
    private void cargarPartidos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Recorrer todos los partidos
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String local = data.child("equipoLocal").getValue(String.class);
                            String visitante = data.child("equipoVisitante").getValue(String.class);

                            int gLocal = Integer.parseInt(data.child("golesLocal").getValue(String.class));
                            int gVisit = Integer.parseInt(data.child("golesVisitante").getValue(String.class));

                            // Obtener los objetos de estadísticas de cada equipo
                            EquipoStats eqLocal = statsMap.get(local);
                            EquipoStats eqVisit = statsMap.get(visitante);

                            // Actualizar goles
                            eqLocal.golesFavor += gLocal;
                            eqLocal.golesContra += gVisit;

                            eqVisit.golesFavor += gVisit;
                            eqVisit.golesContra += gLocal;

                            // Incrementar partidos jugados
                            eqLocal.partidosJugados++;
                            eqVisit.partidosJugados++;

                            // Determinar resultado del partido
                            if (gLocal > gVisit) {
                                // victoria local
                                eqLocal.victorias++;
                                eqLocal.puntos += 3;
                                eqVisit.derrotas++;
                            } else if (gLocal < gVisit) {
                                // victoria visitante
                                eqVisit.victorias++;
                                eqVisit.puntos += 3;
                                eqLocal.derrotas++;
                            } else {
                                // empate
                                eqLocal.empates++;
                                eqVisit.empates++;
                                eqLocal.puntos++;
                                eqVisit.puntos++;
                            }
                        }

                        // Mostrar clasificación final
                        mostrar();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    // Método que ordena y muestra la clasificación
    private void mostrar() {

        tabla.clear();

        // Convertir mapa a lista para poder ordenar
        ArrayList<EquipoStats> lista = new ArrayList<>(statsMap.values());

        // Ordenar por:
        // 1º puntos
        // 2º diferencia de goles
        lista.sort((a, b) -> {
            if (b.puntos != a.puntos) return b.puntos - a.puntos;
            return (b.golesFavor - b.golesContra) - (a.golesFavor - a.golesContra);
        });

        int pos = 1;

        // Construir la clasificación en texto
        for (EquipoStats eq : lista) {

            int dg = eq.golesFavor - eq.golesContra;

            // Iconos para el TOP 3
            String icono;

            if (pos == 1) icono = "🥇 ";
            else if (pos == 2) icono = "🥈 ";
            else if (pos == 3) icono = "🥉 ";
            else icono = pos + ". ";

            // Formato de cada línea de clasificación
            String linea =
                    icono + eq.nombre + "\n" +
                            "PJ:" + eq.partidosJugados +
                            " | Pts:" + eq.puntos +
                            " | V:" + eq.victorias +
                            " E:" + eq.empates +
                            " D:" + eq.derrotas + "\n" +
                            "GF:" + eq.golesFavor +
                            " GC:" + eq.golesContra +
                            " DG:" + (dg >= 0 ? "+" + dg : dg) + "\n" +
                            "-------------------------";

            tabla.add(linea);

            pos++;
        }

        // Refrescar la lista
        adapter.notifyDataSetChanged();
    }

    // Clase que representa las estadísticas de un equipo
    class EquipoStats {
        String nombre;

        int puntos = 0;
        int victorias = 0;
        int empates = 0;
        int derrotas = 0;

        int golesFavor = 0;
        int golesContra = 0;

        int partidosJugados = 0;

        public EquipoStats(String nombre) {
            this.nombre = nombre;
        }
    }
}
