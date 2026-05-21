package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class LigasActivity extends MenuActivity {

    private ListView listLigas;

    private ArrayList<String> ligas;
    private ArrayList<String> ids;
    private ArrayAdapter<String> adapter;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ligas);

        listLigas = findViewById(R.id.listLigas);

        ligas = new ArrayList<>();
        ids = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ligas);

        listLigas.setAdapter(adapter);

        // ✅ CARGAR LIGAS CON CONTROL DE ROLES
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        ligas.clear();
                        ids.clear();

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                        boolean admin = esAdmin(email);

                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);
                            String creador = data.child("creador").getValue(String.class);

                            if (admin) {
                                // ✅ admin ve todas las ligas
                                ligas.add(nombre);
                                ids.add(data.getKey());
                            } else {
                                // ✅ usuario normal solo sus ligas
                                if (creador != null && creador.equals(userId)) {
                                    ligas.add(nombre);
                                    ids.add(data.getKey());
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(LigasActivity.this, "Error al cargar ligas", Toast.LENGTH_SHORT).show();
                    }
                });

        // ✅ CLICK → EQUIPOS
        listLigas.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;

            Intent intent = new Intent(this, EquiposLigaActivity.class);
            intent.putExtra("idLiga", ids.get(position));
            startActivity(intent);
        });

        // ✅ LONG CLICK → BORRAR LIGA
        listLigas.setOnItemLongClickListener((parent, view, position, id) -> {

            String idLiga = ids.get(position);

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar liga")
                    .setMessage("¿Seguro que quieres eliminar esta liga?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        FirebaseDatabase.getInstance()
                                .getReference("ligas")
                                .child(idLiga)
                                .removeValue();

                        Toast.makeText(this, "Liga eliminada", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });
    }

    // ✅ FUNCIÓN PARA SABER SI ES ADMIN
    private boolean esAdmin(String email) {
        return email.equals("vrobledos01@gmail.com") ||
                email.equals("jsierraf01@educarex.es");
    }
}
