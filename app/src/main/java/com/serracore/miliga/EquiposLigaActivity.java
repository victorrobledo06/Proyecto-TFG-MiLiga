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

public class EquiposLigaActivity extends MenuActivity {

    private EditText etNombreEquipo;
    private Button btnGuardarEquipo, btnIrPartidos;
    private ListView listEquipos;

    private ArrayList<String> equipos;
    private ArrayList<String> ids;
    private ArrayAdapter<String> adapter;

    private String idLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos_liga);

        etNombreEquipo = findViewById(R.id.etNombreEquipo);
        btnGuardarEquipo = findViewById(R.id.btnGuardarEquipo);
        btnIrPartidos = findViewById(R.id.btnIrPartidos);
        listEquipos = findViewById(R.id.listEquipos);

        equipos = new ArrayList<>();
        ids = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, equipos);

        listEquipos.setAdapter(adapter);

        idLiga = getIntent().getStringExtra("idLiga");

        // ✅ GUARDAR EQUIPO
        btnGuardarEquipo.setOnClickListener(v -> {

            String nombre = etNombreEquipo.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombreEquipo.setError("Introduce nombre");
                return;
            }

            String idEquipo = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(idLiga)
                    .child("equipos")
                    .push()
                    .getKey();

            HashMap<String, Object> equipo = new HashMap<>();
            equipo.put("nombre", nombre);

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

        // ✅ CARGAR EQUIPOS
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .child(idLiga)
                .child("equipos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        equipos.clear();
                        ids.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);

                            equipos.add(nombre);
                            ids.add(data.getKey());
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // ✅ CLICK → JUGADORES
        listEquipos.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(this, JugadoresActivity.class);
            intent.putExtra("idLiga", idLiga);
            intent.putExtra("idEquipo", ids.get(position));
            startActivity(intent);
        });

        // ✅ BOTÓN → PARTIDOS DE ESTA LIGA
        btnIrPartidos.setOnClickListener(v -> {

            Intent intent = new Intent(this, PartidosActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });

        Button btnEstadisticas = findViewById(R.id.btnEstadisticas);

        btnEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, EstadisticasActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });
        Button btnResultados = findViewById(R.id.btnResultados);

        btnResultados.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultadosActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });

        Button btnClasificacion = findViewById(R.id.btnClasificacion);

        btnClasificacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClasificacionActivity.class);
            intent.putExtra("idLiga", idLiga);
            startActivity(intent);
        });

    }
}