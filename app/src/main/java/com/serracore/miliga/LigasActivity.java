package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

// Activity encargada de mostrar las ligas.
// Implementa control de acceso según el tipo de usuario (admin o normal).
public class LigasActivity extends MenuActivity {

    private ListView listLigas;

    // Lista con los nombres de ligas que se muestran en la interfaz
    private ArrayList<String> ligas;

    // Lista con los IDs reales de las ligas (necesarios para trabajar con Firebase)
    private ArrayList<String> ids;

    private ArrayAdapter<String> adapter;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ligas);

        // Referencia al ListView
        listLigas = findViewById(R.id.listLigas);

        // Inicialización de listas
        ligas = new ArrayList<>();
        ids = new ArrayList<>();

        // Adaptador para mostrar datos en la lista
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ligas);

        listLigas.setAdapter(adapter);

        // Cargar ligas desde Firebase con control de roles
        FirebaseDatabase.getInstance()
                .getReference("ligas")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Limpiar listas antes de volver a cargar datos
                        ligas.clear();
                        ids.clear();

                        // Obtener información del usuario actual
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                        // Comprobar si es administrador
                        boolean admin = esAdmin(email);

                        // Recorrer todas las ligas almacenadas en Firebase
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String nombre = data.child("nombre").getValue(String.class);
                            String creador = data.child("creador").getValue(String.class);

                            if (admin) {
                                // Si es administrador, se muestran todas las ligas
                                ligas.add(nombre);
                                ids.add(data.getKey());

                            } else {
                                // Si es usuario normal, solo muestra las ligas que ha creado
                                if (creador != null && creador.equals(userId)) {
                                    ligas.add(nombre);
                                    ids.add(data.getKey());
                                }
                            }
                        }

                        // Actualizar la lista en pantalla
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(LigasActivity.this,
                                "Error al cargar ligas",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Evento al hacer click en una liga → acceder a sus equipos
        listLigas.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;

            Intent intent = new Intent(this, EquiposLigaActivity.class);
            intent.putExtra("idLiga", ids.get(position));
            startActivity(intent);
        });

        // Evento de pulsación larga → eliminar liga
        listLigas.setOnItemLongClickListener((parent, view, position, id) -> {

            String idLiga = ids.get(position);

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Eliminar liga")
                    .setMessage("¿Seguro que quieres eliminar esta liga?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        // Eliminar la liga en Firebase
                        FirebaseDatabase.getInstance()
                                .getReference("ligas")
                                .child(idLiga)
                                .removeValue();

                        Toast.makeText(this,
                                "Liga eliminada",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });
    }

    // Método que determina si el usuario es administrador
    private boolean esAdmin(String email) {
        return email.equals("vrobledos01@gmail.com") ||
                email.equals("jsierraf01@educarex.es");
    }
}