package com.serracore.miliga;

import android.os.Bundle;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PartidosActivity extends MenuActivity {

    private Spinner spinnerLocal, spinnerVisitante;
    private EditText etGolesLocal, etGolesVisitante;
    private EditText etGoleador, etAsistente, etAmarilla, etRoja;
    private Button btnGuardarPartido;
    private ListView listPartidos;

    private ArrayList<String> equipos, partidos;
    private ArrayAdapter<String> adapterEquipos, adapterPartidos;

    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos);

        spinnerLocal = findViewById(R.id.spinnerLocal);
        spinnerVisitante = findViewById(R.id.spinnerVisitante);
        etGolesLocal = findViewById(R.id.etGolesLocal);
        etGolesVisitante = findViewById(R.id.etGolesVisitante);

        etGoleador = findViewById(R.id.etGoleador);
        etAsistente = findViewById(R.id.etAsistente);
        etAmarilla = findViewById(R.id.etAmarilla);
        etRoja = findViewById(R.id.etRoja);

        btnGuardarPartido = findViewById(R.id.btnGuardarPartido);
        listPartidos = findViewById(R.id.listPartidos);

        equipos = new ArrayList<>();
        partidos = new ArrayList<>();

        adapterEquipos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, equipos);
        adapterEquipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLocal.setAdapter(adapterEquipos);
        spinnerVisitante.setAdapter(adapterEquipos);

        adapterPartidos = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, partidos);

        listPartidos.setAdapter(adapterPartidos);

        idLiga = getIntent().getStringExtra("idLiga");

        // ✅ CARGAR EQUIPOS
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        equipos.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            String nombre = data.child("nombre").getValue(String.class);
                            equipos.add(nombre);
                        }

                        adapterEquipos.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // ✅ GUARDAR PARTIDO COMPLETO
        btnGuardarPartido.setOnClickListener(v -> {

            String local = spinnerLocal.getSelectedItem().toString();
            String visitante = spinnerVisitante.getSelectedItem().toString();
            String golesL = etGolesLocal.getText().toString();
            String golesV = etGolesVisitante.getText().toString();

            String goleador = etGoleador.getText().toString();
            String asistente = etAsistente.getText().toString();
            String amarilla = etAmarilla.getText().toString();
            String roja = etRoja.getText().toString();

            if (golesL.isEmpty() || golesV.isEmpty()) {
                Toast.makeText(this, "Introduce goles", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("partidos")
                    .push().getKey();

            HashMap<String, Object> partido = new HashMap<>();
            partido.put("equipoLocal", local);
            partido.put("equipoVisitante", visitante);
            partido.put("golesLocal", golesL);
            partido.put("golesVisitante", golesV);

            // 🔥 ESTADÍSTICAS
            partido.put("goleador", goleador);
            partido.put("asistente", asistente);
            partido.put("amarilla", amarilla);
            partido.put("roja", roja);

            FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("partidos")
                    .child(id)
                    .setValue(partido);

            Toast.makeText(this, "Partido guardado", Toast.LENGTH_SHORT).show();
        });

        // ✅ CARGAR PARTIDOS
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        partidos.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String l = data.child("equipoLocal").getValue(String.class);
                            String v = data.child("equipoVisitante").getValue(String.class);
                            String gl = data.child("golesLocal").getValue(String.class);
                            String gv = data.child("golesVisitante").getValue(String.class);

                            String goleador = data.child("goleador").getValue(String.class);

                            partidos.add(l + " " + gl + "-" + gv + " " + v +
                                    " | Gol: " + goleador);
                        }

                        adapterPartidos.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
