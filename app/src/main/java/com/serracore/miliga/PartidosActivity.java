package com.serracore.miliga;

import android.os.Bundle;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class PartidosActivity extends MenuActivity {

    private Spinner spinnerLocal, spinnerVisitante, spinnerJugador;

    private EditText etGolesLocal, etGolesVisitante;
    private Button btnGuardarPartido;

    private Button btnAddGol, btnAddAsistencia, btnAddAmarilla, btnAddRoja;

    private ArrayList<String> equipos = new ArrayList<>();
    private ArrayList<String> jugadores = new ArrayList<>();

    private ArrayList<String> goles = new ArrayList<>();
    private ArrayList<String> asistencias = new ArrayList<>();
    private ArrayList<String> amarillas = new ArrayList<>();
    private ArrayList<String> rojas = new ArrayList<>();

    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos);

        spinnerLocal = findViewById(R.id.spinnerLocal);
        spinnerVisitante = findViewById(R.id.spinnerVisitante);
        spinnerJugador = findViewById(R.id.spinnerJugador);

        etGolesLocal = findViewById(R.id.etGolesLocal);
        etGolesVisitante = findViewById(R.id.etGolesVisitante);

        btnGuardarPartido = findViewById(R.id.btnGuardarPartido);

        btnAddGol = findViewById(R.id.btnAddGol);
        btnAddAsistencia = findViewById(R.id.btnAddAsistencia);
        btnAddAmarilla = findViewById(R.id.btnAddAmarilla);
        btnAddRoja = findViewById(R.id.btnAddRoja);

        idLiga = getIntent().getStringExtra("idLiga");

        cargarDatos();

        btnAddGol.setOnClickListener(v -> agregarEvento(goles, "Gol"));
        btnAddAsistencia.setOnClickListener(v -> agregarEvento(asistencias, "Asistencia"));
        btnAddAmarilla.setOnClickListener(v -> agregarEvento(amarillas, "Amarilla"));
        btnAddRoja.setOnClickListener(v -> agregarEvento(rojas, "Roja"));

        btnGuardarPartido.setOnClickListener(v -> guardarPartido());
    }

    private void agregarEvento(ArrayList<String> lista, String tipo) {
        String jugador = spinnerJugador.getSelectedItem().toString();

        if (jugador.equals("Selecciona jugador")) {
            Toast.makeText(this, "Selecciona un jugador", Toast.LENGTH_SHORT).show();
            return;
        }

        lista.add(jugador);
        Toast.makeText(this, tipo + " añadido: " + jugador, Toast.LENGTH_SHORT).show();
    }

    private void guardarPartido() {

        String local = spinnerLocal.getSelectedItem().toString();
        String visitante = spinnerVisitante.getSelectedItem().toString();

        String golesL = etGolesLocal.getText().toString();
        String golesV = etGolesVisitante.getText().toString();

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

        partido.put("goles", goles);
        partido.put("asistencias", asistencias);
        partido.put("amarillas", amarillas);
        partido.put("rojas", rojas);
        partido.put("jornada", "1");

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .child(id)
                .setValue(partido);

        Toast.makeText(this, "Partido guardado", Toast.LENGTH_SHORT).show();
    }

    private void cargarDatos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        jugadores.add("Selecciona jugador");

                        for (DataSnapshot eq : snapshot.getChildren()) {

                            String nombreEq = eq.child("nombre").getValue(String.class);
                            equipos.add(nombreEq);

                            for (DataSnapshot jug : eq.child("jugadores").getChildren()) {
                                String nombreJug = jug.child("nombre").getValue(String.class);
                                jugadores.add(nombreJug);
                            }
                        }

                        spinnerLocal.setAdapter(new ArrayAdapter<>(PartidosActivity.this,
                                android.R.layout.simple_spinner_item, equipos));

                        spinnerVisitante.setAdapter(new ArrayAdapter<>(PartidosActivity.this,
                                android.R.layout.simple_spinner_item, equipos));

                        spinnerJugador.setAdapter(new ArrayAdapter<>(PartidosActivity.this,
                                android.R.layout.simple_spinner_item, jugadores));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
