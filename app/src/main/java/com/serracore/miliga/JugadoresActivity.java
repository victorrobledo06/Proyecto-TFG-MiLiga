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

// Activity encargada de gestionar los jugadores de un equipo.
// Permite añadir jugadores, mostrar la lista y eliminarlos.
public class JugadoresActivity extends MenuActivity {

    private EditText etNombre, etPosicion, etDorsal;
    private Button btnGuardarJugador;
    private ListView listJugadores;

    // Lista que contiene los datos que se muestran en pantalla
    private ArrayList<String> jugadores;

    // Lista que almacena los IDs reales de Firebase (necesarios para eliminar)
    private ArrayList<String> ids;

    private ArrayAdapter<String> adapter;

    // IDs necesarios para acceder a la base de datos
    private String idLiga, idEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores);

        // Título de la pantalla
        setTitle("Jugadores - MiLiga");

        // Referencias a los elementos del layout
        etNombre = findViewById(R.id.etNombreJugador);
        etPosicion = findViewById(R.id.etPosicion);
        etDorsal = findViewById(R.id.etDorsal);
        btnGuardarJugador = findViewById(R.id.btnGuardarJugador);
        listJugadores = findViewById(R.id.listJugadores);

        // Inicialización de listas
        jugadores = new ArrayList<>();
        ids = new ArrayList<>();

        // Adaptador para mostrar los jugadores en el ListView
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jugadores);

        listJugadores.setAdapter(adapter);

        // Obtener datos de la activity anterior
        idLiga = getIntent().getStringExtra("idLiga");
        idEquipo = getIntent().getStringExtra("idEquipo");

        // GUARDAR JUGADOR
        btnGuardarJugador.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String posicion = etPosicion.getText().toString().trim();
            String dorsal = etDorsal.getText().toString().trim();

            // Validación de que los campos no estén vacíos
            if (nombre.isEmpty() || posicion.isEmpty() || dorsal.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generar ID único para el jugador en Firebase
            String idJugador = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("equipos")
                    .child(idEquipo)
                    .child("jugadores")
                    .push()
                    .getKey();

            // Crear objeto con la información del jugador
            HashMap<String, Object> jugador = new HashMap<>();
            jugador.put("nombre", nombre);
            jugador.put("posicion", posicion);
            jugador.put("dorsal", dorsal);

            // Guardar jugador en Firebase
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

                            // Mensaje de éxito
                            Toast.makeText(this, "✅ Jugador añadido", Toast.LENGTH_SHORT).show();

                            // Limpiar campos de texto
                            etNombre.setText("");
                            etPosicion.setText("");
                            etDorsal.setText("");

                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // CARGAR JUGADORES DESDE FIREBASE
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .child(idEquipo)
                .child("jugadores")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Limpiar listas antes de cargar nuevos datos
                        jugadores.clear();
                        ids.clear();

                        // Recorrer todos los jugadores guardados
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);
                            String posicion = data.child("posicion").getValue(String.class);
                            String dorsal = data.child("dorsal").getValue(String.class);

                            // Formato del texto que se muestra en pantalla
                            jugadores.add(nombre + " - " + posicion + " (Nº " + dorsal + ")");

                            // Guardar ID para futuras operaciones (ej: eliminar)
                            ids.add(data.getKey());
                        }

                        // Si no hay jugadores, mostrar mensaje
                        if (jugadores.isEmpty()) {
                            jugadores.add("No hay jugadores disponibles");
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // BORRAR JUGADOR (PULSACIÓN LARGA)
        listJugadores.setOnItemLongClickListener((parent, view, position, id) -> {

            // Evitar errores si la lista está vacía
            if (ids.size() == 0) return true;

            String idJugador = ids.get(position);

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar jugador")
                    .setMessage("¿Seguro que quieres eliminar este jugador?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        // Eliminar jugador de Firebase
                        FirebaseDatabase.getInstance()
                                .getReference("ligas")
                                .child(idLiga)
                                .child("equipos")
                                .child(idEquipo)
                                .child("jugadores")
                                .child(idJugador)
                                .removeValue();

                        Toast.makeText(this, "🗑️ Jugador eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });
    }
}