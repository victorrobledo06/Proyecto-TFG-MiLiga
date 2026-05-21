package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth; // ✅ IMPORTANTE
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GestionarActivity extends MenuActivity {

    private EditText etNombreLiga;
    private Button btnGuardarLiga, btnVerLigas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar);

        etNombreLiga = findViewById(R.id.etNombreLiga);
        btnGuardarLiga = findViewById(R.id.btnGuardarLiga);
        btnVerLigas = findViewById(R.id.btnVerLigas);

        // ✅ GUARDAR LIGA CON CREADOR
        btnGuardarLiga.setOnClickListener(v -> {

            String nombre = etNombreLiga.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombreLiga.setError("Introduce un nombre");
                return;
            }

            String id = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .push()
                    .getKey();

            // ✅ obtener usuario actual
            String userId = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid();

            HashMap<String, Object> liga = new HashMap<>();
            liga.put("nombre", nombre);
            liga.put("creador", userId); // 🔥 CLAVE

            FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(id)
                    .setValue(liga)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(this,
                                    "✅ Liga guardada",
                                    Toast.LENGTH_SHORT).show();

                            etNombreLiga.setText("");

                        } else {

                            Toast.makeText(this,
                                    "Error al guardar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // ✅ IR A VER LIGAS
        btnVerLigas.setOnClickListener(v ->
                startActivity(new Intent(this, LigasActivity.class)));
    }
}