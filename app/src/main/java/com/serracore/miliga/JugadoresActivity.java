package com.serracore.miliga;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class JugadoresActivity extends MenuActivity {

    private EditText etNombre, etPosicion, etDorsal;
    private Button btnGuardarJugador;
    private ListView listJugadores;

    private ArrayList<String> jugadores;
    private ArrayAdapter<String> adapter;

    private String idLiga, idEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores);

        etNombre = findViewById(R.id.etNombreJugador);
        etPosicion = findViewById(R.id.etPosicion);
        etDorsal = findViewById(R.id.etDorsal);
        btnGuardarJugador = findViewById(R.id.btnGuardarJugador);
        listJugadores = findViewById(R.id.listJugadores);

        jugadores = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jugadores);

        listJugadores.setAdapter(adapter);

        // Recibir IDs
        idLiga = getIntent().getStringExtra("idLiga");
        idEquipo = getIntent().getStringExtra("idEquipo");

        // GUARDAR JUGADOR
        btnGuardarJugador.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String posicion = etPosicion.getText().toString().trim();
            String dorsal = etDorsal.getText().toString().trim();

            if (nombre.isEmpty() || posicion.isEmpty() || dorsal.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            String idJugador = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("equipos")
                    .child(idEquipo)
                    .child("jugadores")
                    .push()
                    .getKey();

            HashMap<String, Object> jugador = new HashMap<>();
            jugador.put("nombre", nombre);
            jugador.put("posicion", posicion);
            jugador.put("dorsal", dorsal);

            FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("equipos")
                    .child(idEquipo)
                    .child("jugadores")
                    .child(idJugador)
                    .setValue(jugador)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Jugador añadido", Toast.LENGTH_SHORT).show();
                            etNombre.setText("");
                            etPosicion.setText("");
                            etDorsal.setText("");
                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // CARGAR JUGADORES
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .child(idEquipo)
                .child("jugadores")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        jugadores.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);
                            String posicion = data.child("posicion").getValue(String.class);
                            String dorsal = data.child("dorsal").getValue(String.class);

                            jugadores.add(nombre + " - " + posicion + " (Nº " + dorsal + ")");
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }
}