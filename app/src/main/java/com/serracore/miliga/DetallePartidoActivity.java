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

                        cargarJugadoresPorEquipo(local, visitante, data);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void cargarJugadoresPorEquipo(String local,
                                          String visitante,
                                          DataSnapshot partidoData) {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        ArrayList<String> jugadoresLocal = new ArrayList<>();
                        ArrayList<String> jugadoresVisitante = new ArrayList<>();

                        for (DataSnapshot eq : snapshot.getChildren()) {

                            String nombreEquipo = eq.child("nombre").getValue(String.class);

                            if (nombreEquipo.equals(local)) {
                                for (DataSnapshot jug : eq.child("jugadores").getChildren()) {
                                    jugadoresLocal.add(jug.child("nombre").getValue(String.class));
                                }
                            }

                            if (nombreEquipo.equals(visitante)) {
                                for (DataSnapshot jug : eq.child("jugadores").getChildren()) {
                                    jugadoresVisitante.add(jug.child("nombre").getValue(String.class));
                                }
                            }
                        }

                        mostrarEventosSeparados(jugadoresLocal, jugadoresVisitante, local, visitante, partidoData);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void mostrarEventosSeparados(ArrayList<String> jugLocal,
                                         ArrayList<String> jugVisit,
                                         String local,
                                         String visitante,
                                         DataSnapshot data) {

        eventos.clear();

        // 🔵 EQUIPO LOCAL
        eventos.add("🔵 " + local);

        añadirEventosOrdenados(data, "goles", "⚽ ", jugLocal);
        añadirEventosOrdenados(data, "asistencias", "🎯 ", jugLocal);
        añadirEventosOrdenados(data, "amarillas", "🟨 ", jugLocal);
        añadirEventosOrdenados(data, "rojas", "🟥 ", jugLocal);

        eventos.add("");

        // 🔴 EQUIPO VISITANTE
        eventos.add("🔴 " + visitante);

        añadirEventosOrdenados(data, "goles", "⚽ ", jugVisit);
        añadirEventosOrdenados(data, "asistencias", "🎯 ", jugVisit);
        añadirEventosOrdenados(data, "amarillas", "🟨 ", jugVisit);
        añadirEventosOrdenados(data, "rojas", "🟥 ", jugVisit);

        if (eventos.size() == 0) {
            eventos.add("No hay eventos en este partido");
        }

        adapter.notifyDataSetChanged();
    }

    private void añadirEventosOrdenados(DataSnapshot data,
                                        String campo,
                                        String icono,
                                        ArrayList<String> jugadoresEquipo) {

        if (!data.hasChild(campo)) return;

        ArrayList<String> eventosTemp = new ArrayList<>();

        for (DataSnapshot item : data.child(campo).getChildren()) {

            String evento = item.getValue(String.class);
            if (evento == null) continue;

            // ✅ SOLUCIÓN ROBUSTA (FUNCIONA CON EMOJIS Y PENALTIS)
            String nombre;
            try {
                nombre = evento.split("\\(")[0].trim();
            } catch (Exception e) {
                nombre = evento;
            }

            if (jugadoresEquipo.contains(nombre)) {
                eventosTemp.add(icono + evento);
            }
        }

        // ✅ ORDENAR POR MINUTO
        eventosTemp.sort((a, b) -> {
            int minA = extraerMinuto(a);
            int minB = extraerMinuto(b);
            return minA - minB;
        });

        eventos.addAll(eventosTemp);
    }

    private int extraerMinuto(String evento) {

        try {
            int inicio = evento.indexOf("(");
            int fin = evento.indexOf("'");

            if (inicio != -1 && fin != -1) {
                return Integer.parseInt(evento.substring(inicio + 1, fin));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
