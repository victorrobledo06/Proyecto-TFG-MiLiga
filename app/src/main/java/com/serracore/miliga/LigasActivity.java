package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class LigasActivity extends MenuActivity {

    private ListView listLigas;
    private Button btnVerPartidos;

    private ArrayList<String> ligas;
    private ArrayList<String> ids;
    private ArrayAdapter<String> adapter;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ligas);

        listLigas = findViewById(R.id.listLigas);
        btnVerPartidos = findViewById(R.id.btnVerPartidos);

        ligas = new ArrayList<>();
        ids = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ligas);

        listLigas.setAdapter(adapter);

        // ✅ CARGAR LIGAS
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        ligas.clear();
                        ids.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);

                            ligas.add(nombre);
                            ids.add(data.getKey());
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // ✅ CLICK → EQUIPOS
        listLigas.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;

            Intent intent = new Intent(this, EquiposLigaActivity.class);
            intent.putExtra("idLiga", ids.get(position));
            startActivity(intent);
        });

        // ✅ BOTÓN → PARTIDOS
        btnVerPartidos.setOnClickListener(v -> {

            if (selectedPosition == -1) {
                Toast.makeText(this, "Selecciona una liga primero", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, PartidosActivity.class);
            intent.putExtra("idLiga", ids.get(selectedPosition));
            startActivity(intent);
        });
    }
}
