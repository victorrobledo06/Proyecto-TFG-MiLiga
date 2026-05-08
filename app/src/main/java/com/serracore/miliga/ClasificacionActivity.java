package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClasificacionActivity extends MenuActivity {

    private ListView listClasificacion;
    private ArrayList<String> tabla;
    private ArrayAdapter<String> adapter;

    private HashMap<String, EquipoStats> statsMap;

    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);

        listClasificacion = findViewById(R.id.listClasificacion);

        tabla = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, tabla);

        listClasificacion.setAdapter(adapter);

        statsMap = new HashMap<>();

        idLiga = getIntent().getStringExtra("idLiga");

        cargarEquipos();
    }

    private void cargarEquipos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot data : snapshot.getChildren()) {
                            String nombre = data.child("nombre").getValue(String.class);
                            statsMap.put(nombre, new EquipoStats(nombre));
                        }

                        cargarPartidos();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
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

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String local = data.child("equipoLocal").getValue(String.class);
                            String visitante = data.child("equipoVisitante").getValue(String.class);

                            int gLocal = Integer.parseInt(data.child("golesLocal").getValue(String.class));
                            int gVisit = Integer.parseInt(data.child("golesVisitante").getValue(String.class));

                            EquipoStats eqLocal = statsMap.get(local);
                            EquipoStats eqVisit = statsMap.get(visitante);

                            eqLocal.golesFavor += gLocal;
                            eqLocal.golesContra += gVisit;

                            eqVisit.golesFavor += gVisit;
                            eqVisit.golesContra += gLocal;

                            eqLocal.partidosJugados++;
                            eqVisit.partidosJugados++;

                            if (gLocal > gVisit) {
                                eqLocal.victorias++;
                                eqLocal.puntos += 3;
                                eqVisit.derrotas++;
                            } else if (gLocal < gVisit) {
                                eqVisit.victorias++;
                                eqVisit.puntos += 3;
                                eqLocal.derrotas++;
                            } else {
                                eqLocal.empates++;
                                eqVisit.empates++;
                                eqLocal.puntos++;
                                eqVisit.puntos++;
                            }
                        }

                        mostrar();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void mostrar() {

        tabla.clear();

        ArrayList<EquipoStats> lista = new ArrayList<>(statsMap.values());

        lista.sort((a, b) -> {
            if (b.puntos != a.puntos) return b.puntos - a.puntos;
            return (b.golesFavor - b.golesContra) - (a.golesFavor - a.golesContra);
        });

        int pos = 1;

        for (EquipoStats eq : lista) {

            int dg = eq.golesFavor - eq.golesContra;

            String icono;

            if (pos == 1) icono = "🥇 ";
            else if (pos == 2) icono = "🥈 ";
            else if (pos == 3) icono = "🥉 ";
            else icono = pos + ". ";

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

        adapter.notifyDataSetChanged();
    }


    class EquipoStats {
        String nombre;
        int puntos = 0;
        int victorias = 0;
        int empates = 0;
        int derrotas = 0;
        int golesFavor = 0;
        int golesContra = 0;
        int partidosJugados = 0; // 🔥 NUEVO

        public EquipoStats(String nombre) {
            this.nombre = nombre;
        }
    }


}
