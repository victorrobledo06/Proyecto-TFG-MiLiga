package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        btnGuardarLiga.setOnClickListener(v -> {

            String nombre = etNombreLiga.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombreLiga.setError("Introduce un nombre");
                return;
            }

            String id = FirebaseDatabase.getInstance()
                    .getReference("ligas").push().getKey();

            HashMap<String, Object> liga = new HashMap<>();
            liga.put("nombre", nombre);

            FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(id)
                    .setValue(liga)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(this, "Liga guardada", Toast.LENGTH_SHORT).show();
                            etNombreLiga.setText("");

                        } else {
                            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Botón para ver ligas
        btnVerLigas.setOnClickListener(v ->
                startActivity(new Intent(this, LigasActivity.class)));
    }
}