package com.serracore.miliga;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

// Activity encargada de gestionar la creación de partidos.
// Permite seleccionar equipos, introducir resultado y añadir eventos (goles, asistencias, tarjetas).
public class PartidosActivity extends MenuActivity {

    // Spinners para seleccionar equipos y jugadores
    private Spinner spinnerLocal, spinnerVisitante, spinnerJugador;

    // Campos para introducir resultado del partido
    private EditText etGolesLocal, etGolesVisitante;

    private Button btnGuardarPartido;

    // Botones para añadir eventos del partido
    private Button btnAddGol, btnAddAsistencia, btnAddAmarilla, btnAddRoja;

    // Listas de equipos y jugadores
    private ArrayList<String> equipos = new ArrayList<>();
    private ArrayList<String> jugadores = new ArrayList<>();

    // Listas de eventos del partido
    private ArrayList<String> goles = new ArrayList<>();
    private ArrayList<String> asistencias = new ArrayList<>();
    private ArrayList<String> amarillas = new ArrayList<>();
    private ArrayList<String> rojas = new ArrayList<>();

    private String idLiga;

    // Campos adicionales del partido
    private EditText etJornada;
    private EditText etMinuto;
    private CheckBox checkPenalti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos);

        // Referencias a los elementos del layout
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

        // Obtener ID de la liga
        idLiga = getIntent().getStringExtra("idLiga");

        // Cargar equipos y jugadores desde Firebase
        cargarDatos();

        // Añadir gol
        btnAddGol.setOnClickListener(v -> agregarEvento(goles, "⚽ Gol"));

        // ASISTENCIA AVANZADA
        btnAddAsistencia.setOnClickListener(v -> {

            // No se puede añadir asistencia sin goles
            if (goles.isEmpty()) {
                Toast.makeText(this, "Primero añade un gol", Toast.LENGTH_SHORT).show();
                return;
            }

            String jugador = spinnerJugador.getSelectedItem().toString();

            if (jugador.equals("Selecciona jugador")) {
                Toast.makeText(this, "Selecciona un jugador", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar diálogo para seleccionar a qué gol pertenece la asistencia
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona el gol al que pertenece la asistencia");

            String[] golesArray = goles.toArray(new String[0]);

            builder.setItems(golesArray, (dialog, which) -> {

                String golSeleccionado = goles.get(which);

                // Extraer minuto del gol seleccionado
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

        // Añadir tarjetas
        btnAddAmarilla.setOnClickListener(v -> agregarEvento(amarillas, "🟨 Amarilla"));
        btnAddRoja.setOnClickListener(v -> agregarEvento(rojas, "🟥 Roja"));

        // Guardar el partido
        btnGuardarPartido.setOnClickListener(v -> guardarPartido());
    }

    // Método para añadir eventos (gol, tarjeta, etc.)
    private void agregarEvento(ArrayList<String> lista, String tipo) {

        String jugador = spinnerJugador.getSelectedItem().toString();
        String minuto = etMinuto.getText().toString();

        // Validaciones
        if (jugador.equals("Selecciona jugador")) {
            Toast.makeText(this, "Selecciona un jugador", Toast.LENGTH_SHORT).show();
            return;
        }

        if (minuto.isEmpty()) {
            Toast.makeText(this, "Introduce el minuto", Toast.LENGTH_SHORT).show();
            return;
        }

        String evento;

        // Si es gol y está marcado penalti
        if (tipo.contains("Gol") && checkPenalti.isChecked()) {
            evento = jugador + " (" + minuto + "') ⚽🥅";
        } else {
            evento = jugador + " (" + minuto + "')";
        }

        lista.add(evento);

        Toast.makeText(this, tipo + " ✅ " + evento, Toast.LENGTH_SHORT).show();

        // Limpiar campo de minuto
        etMinuto.setText("");

        // Resetear checkbox de penalti
        checkPenalti.setChecked(false);
    }

    // Confirmación antes de guardar partido
    private void guardarPartido() {

        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("¿Quieres guardar este partido?")
                .setPositiveButton("Sí", (dialog, which) -> guardarPartidoReal())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Guardado real del partido en Firebase
    private void guardarPartidoReal() {

        String local = spinnerLocal.getSelectedItem().toString();
        String visitante = spinnerVisitante.getSelectedItem().toString();

        String golesL = etGolesLocal.getText().toString();
        String golesV = etGolesVisitante.getText().toString();
        String jornada = etJornada.getText().toString();

        // Generar ID del partido
        String id = FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .push()
                .getKey();

        // Crear objeto partido
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

        // Guardar en Firebase
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("partidos")
                .child(id)
                .setValue(partido);

        Toast.makeText(this, "✅ Partido guardado correctamente", Toast.LENGTH_SHORT).show();

        // Limpiar campos
        etGolesLocal.setText("");
        etGolesVisitante.setText("");
        etJornada.setText("");

        goles.clear();
        asistencias.clear();
        amarillas.clear();
        rojas.clear();
    }

    // Método que carga equipos y jugadores desde Firebase
    private void cargarDatos() {

        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        jugadores.add("Selecciona jugador");

                        // Recorrer equipos
                        for (DataSnapshot eq : snapshot.getChildren()) {

                            String nombreEq = eq.child("nombre").getValue(String.class);
                            equipos.add(nombreEq);

                            // Recorrer jugadores de cada equipo
                            for (DataSnapshot jug : eq.child("jugadores").getChildren()) {
                                jugadores.add(jug.child("nombre").getValue(String.class));
                            }
                        }

                        // Asignar datos a los spinners
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
