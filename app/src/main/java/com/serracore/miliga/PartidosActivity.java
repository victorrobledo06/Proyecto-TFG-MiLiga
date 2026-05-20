package com.serracore.miliga;

import android.app.AlertDialog;
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
    private EditText etJornada;
    private EditText etMinuto;
    private CheckBox checkPenalti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos);

        spinnerLocal = findViewById(R.id.spinnerLocal);
        spinnerVisitante = findViewById(R.id.spinnerVisitante);
        spinnerJugador = findViewById(R.id.spinnerJugador);

        etGolesLocal = findViewById(R.id.etGolesLocal);
        etGolesVisitante = findViewById(R.id.etGolesVisitante);
        etJornada = findViewById(R.id.etJornada);
        etMinuto = findViewById(R.id.etMinuto);

        btnGuardarPartido = findViewById(R.id.btnGuardarPartido);

        btnAddGol = findViewById(R.id.btnAddGol);
        btnAddAsistencia = findViewById(R.id.btnAddAsistencia);
        btnAddAmarilla = findViewById(R.id.btnAddAmarilla);
        btnAddRoja = findViewById(R.id.btnAddRoja);

        checkPenalti = findViewById(R.id.checkPenalti);


        idLiga = getIntent().getStringExtra("idLiga");

        cargarDatos();

        btnAddGol.setOnClickListener(v -> agregarEvento(goles, "⚽ Gol"));

        // ✅ ASISTENCIA AVANZADA
        btnAddAsistencia.setOnClickListener(v -> {

            if (goles.isEmpty()) {
                Toast.makeText(this, "Primero añade un gol", Toast.LENGTH_SHORT).show();
                return;
            }

            String jugador = spinnerJugador.getSelectedItem().toString();

            if (jugador.equals("Selecciona jugador")) {
                Toast.makeText(this, "Selecciona un jugador", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ seleccionar gol
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona el gol al que pertenece la asistencia");

            String[] golesArray = goles.toArray(new String[0]);

            builder.setItems(golesArray, (dialog, which) -> {

                String golSeleccionado = goles.get(which);

                String minuto = "";
                if (golSeleccionado.contains("(")) {
                    int inicio = golSeleccionado.indexOf("(");
                    int fin = golSeleccionado.indexOf("'");
                    minuto = golSeleccionado.substring(inicio + 1, fin);
                }

                String evento = jugador + " (" + minuto + "')";

                asistencias.add(evento);

                Toast.makeText(this,
                        "🎯 Asistencia ✅ " + evento,
                        Toast.LENGTH_SHORT).show();
            });

            builder.show();
        });

        btnAddAmarilla.setOnClickListener(v -> agregarEvento(amarillas, "🟨 Amarilla"));
        btnAddRoja.setOnClickListener(v -> agregarEvento(rojas, "🟥 Roja"));

        btnGuardarPartido.setOnClickListener(v -> guardarPartido());
    }

    private void agregarEvento(ArrayList<String> lista, String tipo) {

        String jugador = spinnerJugador.getSelectedItem().toString();
        String minuto = etMinuto.getText().toString();

        if (jugador.equals("Selecciona jugador")) {
            Toast.makeText(this, "Selecciona un jugador", Toast.LENGTH_SHORT).show();
            return;
        }

        if (minuto.isEmpty()) {
            Toast.makeText(this, "Introduce el minuto", Toast.LENGTH_SHORT).show();
            return;
        }

        String evento;

        // ✅ SOLO PARA GOLES
        if (tipo.contains("Gol") && checkPenalti.isChecked()) {
            evento = jugador + " (" + minuto + "') ⚽🥅";
        } else {
            evento = jugador + " (" + minuto + "')";
        }

        lista.add(evento);

        Toast.makeText(this, tipo + " ✅ " + evento, Toast.LENGTH_SHORT).show();

        etMinuto.setText("");

        // ✅ reset penalti después de usarlo
        checkPenalti.setChecked(false);
    }

    private void guardarPartido() {

        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("¿Quieres guardar este partido?")
                .setPositiveButton("Sí", (dialog, which) -> guardarPartidoReal())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarPartidoReal() {

        String local = spinnerLocal.getSelectedItem().toString();
        String visitante = spinnerVisitante.getSelectedItem().toString();

        String golesL = etGolesLocal.getText().toString();
        String golesV = etGolesVisitante.getText().toString();
        String jornada = etJornada.getText().toString();

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
        partido.put("jornada", jornada);

        partido.put("goles", goles);
        partido.put("asistencias", asistencias);
        partido.put("amarillas", amarillas);
        partido.put("rojas", rojas);

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .child(id)
                .setValue(partido);

        Toast.makeText(this, "✅ Partido guardado correctamente", Toast.LENGTH_SHORT).show();

        // limpiar
        etGolesLocal.setText("");
        etGolesVisitante.setText("");
        etJornada.setText("");

        goles.clear();
        asistencias.clear();
        amarillas.clear();
        rojas.clear();
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
                                jugadores.add(jug.child("nombre").getValue(String.class));
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
