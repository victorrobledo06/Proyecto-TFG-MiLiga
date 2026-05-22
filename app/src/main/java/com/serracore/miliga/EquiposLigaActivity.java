package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

// Activity encargada de gestionar los equipos de una liga concreta.
// Permite crear equipos, visualizarlos, eliminarlos y acceder a otras funcionalidades.
public class EquiposLigaActivity extends MenuActivity {

    private EditText etNombreEquipo;
    private Button btnGuardarEquipo, btnIrPartidos;
    private ListView listEquipos;

    // Lista con nombres de equipos (para mostrar)
    private ArrayList<String> equipos;

    // Lista con IDs de equipos (para operaciones en Firebase)
    private ArrayList<String> ids;

    private ArrayAdapter<String> adapter;

    // ID de la liga actual
    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos_liga);

        // Referencias a los elementos de la interfaz
        etNombreEquipo = findViewById(R.id.etNombreEquipo);
        btnGuardarEquipo = findViewById(R.id.btnGuardarEquipo);
        btnIrPartidos = findViewById(R.id.btnIrPartidos);
        listEquipos = findViewById(R.id.listEquipos);

        // Inicialización de listas
        equipos = new ArrayList<>();
        ids = new ArrayList<>();

        // Adaptador para mostrar equipos en el ListView
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, equipos);

        listEquipos.setAdapter(adapter);

        // Obtener ID de la liga desde la activity anterior
        idLiga = getIntent().getStringExtra("idLiga");

        // Botón para guardar un nuevo equipo
        btnGuardarEquipo.setOnClickListener(v -> {

            String nombre = etNombreEquipo.getText().toString().trim();

            // Validación del campo
            if (nombre.isEmpty()) {
                etNombreEquipo.setError("Introduce nombre");
                return;
            }

            // Generar ID automático para el equipo
            String idEquipo = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("equipos")
                    .push()
                    .getKey();

            // Crear objeto equipo
            HashMap<String, Object> equipo = new HashMap<>();
            equipo.put("nombre", nombre);

            // Guardar equipo en Firebase
            FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("equipos")
                    .child(idEquipo)
                    .setValue(equipo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Equipo añadido", Toast.LENGTH_SHORT).show();
                            etNombreEquipo.setText("");
                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Cargar equipos desde Firebase en tiempo real
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Limpiar listas antes de recargar datos
                        equipos.clear();
                        ids.clear();

                        // Recorrer todos los equipos
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);

                            equipos.add(nombre);
                            ids.add(data.getKey()); // Guardar ID correspondiente
                        }

                        // Refrescar lista
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // Evento al hacer click en un equipo → ir a jugadores
        listEquipos.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(this, JugadoresActivity.class);
            intent.putExtra("idLiga", idLiga);
            intent.putExtra("idEquipo", ids.get(position));
            startActivity(intent);
        });

        // Evento de pulsación larga → eliminar equipo
        listEquipos.setOnItemLongClickListener((parent, view, position, id) -> {

            String idEquipo = ids.get(position);

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar equipo")
                    .setMessage("¿Seguro que quieres eliminar este equipo?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        FirebaseDatabase.getInstance()
                                .getReference("ligas")
                                .child(idLiga)
                                .child("equipos")
                                .child(idEquipo)
                                .removeValue();

                        Toast.makeText(this, "Equipo eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });

        // Botón para ir a la gestión de partidos
        btnIrPartidos.setOnClickListener(v -> {

            Intent intent = new Intent(this, PartidosActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });

        // Botón para ver estadísticas
        Button btnEstadisticas = findViewById(R.id.btnEstadisticas);
        btnEstadisticas.setOnClickListener(v -> {

            Intent intent = new Intent(this, EstadisticasActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });

        // Botón para ver resultados
        Button btnResultados = findViewById(R.id.btnResultados);
        btnResultados.setOnClickListener(v -> {

            Intent intent = new Intent(this, ResultadosActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });

        // Botón para ver clasificación
        Button btnClasificacion = findViewById(R.id.btnClasificacion);
        btnClasificacion.setOnClickListener(v -> {

            Intent intent = new Intent(this, ClasificacionActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });
    }
}
