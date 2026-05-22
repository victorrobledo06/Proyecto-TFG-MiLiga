package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.*;

import java.util.ArrayList;

// Activity que muestra el detalle de un partido:
// marcador + eventos (goles, asistencias, tarjetas)
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

        // Referencias a los elementos de la interfaz
        tvTitulo = findViewById(R.id.tvTituloPartido);
        listEventos = findViewById(R.id.listEventos);

        // Inicialización de la lista de eventos
        eventos = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, eventos);

        listEventos.setAdapter(adapter);

        // Recibir datos de la activity anterior
        idLiga = getIntent().getStringExtra("idLiga");
        idPartido = getIntent().getStringExtra("idPartido");

        // Cargar datos del partido desde Firebase
        cargarPartido();
    }

    // Método que obtiene los datos del partido (equipos y resultado)
    private void cargarPartido() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .child(idPartido)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot data) {

                        // Obtener datos básicos del partido
                        String local = data.child("equipoLocal").getValue(String.class);
                        String visitante = data.child("equipoVisitante").getValue(String.class);

                        String gl = data.child("golesLocal").getValue(String.class);
                        String gv = data.child("golesVisitante").getValue(String.class);

                        // Mostrar marcador en pantalla
                        tvTitulo.setText(local + " " + gl + " - " + gv + " " + visitante);

                        // Cargar jugadores para poder separar eventos por equipo
                        cargarJugadoresPorEquipo(local, visitante, data);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    // Método que obtiene los jugadores de cada equipo desde Firebase
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

                        // Recorrer equipos de la liga
                        for (DataSnapshot eq : snapshot.getChildren()) {

                            String nombreEquipo = eq.child("nombre").getValue(String.class);

                            // Si es equipo local → guardar sus jugadores
                            if (nombreEquipo.equals(local)) {
                                for (DataSnapshot jug : eq.child("jugadores").getChildren()) {
                                    jugadoresLocal.add(jug.child("nombre").getValue(String.class));
                                }
                            }

                            // Si es equipo visitante → guardar sus jugadores
                            if (nombreEquipo.equals(visitante)) {
                                for (DataSnapshot jug : eq.child("jugadores").getChildren()) {
                                    jugadoresVisitante.add(jug.child("nombre").getValue(String.class));
                                }
                            }
                        }

                        // Mostrar los eventos separados por equipo
                        mostrarEventosSeparados(jugadoresLocal, jugadoresVisitante, local, visitante, partidoData);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    // Método que muestra los eventos del partido agrupados por equipo
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

        eventos.add(""); // espacio visual

        // 🔴 EQUIPO VISITANTE
        eventos.add("🔴 " + visitante);

        añadirEventosOrdenados(data, "goles", "⚽ ", jugVisit);
        añadirEventosOrdenados(data, "asistencias", "🎯 ", jugVisit);
        añadirEventosOrdenados(data, "amarillas", "🟨 ", jugVisit);
        añadirEventosOrdenados(data, "rojas", "🟥 ", jugVisit);

        // Si no hay eventos
        if (eventos.size() == 0) {
            eventos.add("No hay eventos en este partido");
        }

        adapter.notifyDataSetChanged();
    }

    // Método que filtra y ordena eventos por minuto
    private void añadirEventosOrdenados(DataSnapshot data,
                                        String campo,
                                        String icono,
                                        ArrayList<String> jugadoresEquipo) {

        if (!data.hasChild(campo)) return;

        ArrayList<String> eventosTemp = new ArrayList<>();

        for (DataSnapshot item : data.child(campo).getChildren()) {

            String evento = item.getValue(String.class);
            if (evento == null) continue;

            // Extraer el nombre del jugador (ignorando minuto y emojis)
            String nombre;
            try {
                nombre = evento.split("\\(")[0].trim();
            } catch (Exception e) {
                nombre = evento;
            }

            // Comprobar que el evento pertenece a este equipo
            if (jugadoresEquipo.contains(nombre)) {
                eventosTemp.add(icono + evento);
            }
        }

        // Ordenar eventos por minuto
        eventosTemp.sort((a, b) -> {
            int minA = extraerMinuto(a);
            int minB = extraerMinuto(b);
            return minA - minB;
        });

        eventos.addAll(eventosTemp);
    }

    // Método que extrae el minuto de un evento (ej: "Jugador (23')")
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