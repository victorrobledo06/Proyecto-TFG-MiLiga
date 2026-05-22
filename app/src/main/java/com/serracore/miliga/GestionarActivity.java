package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// Activity encargada de crear nuevas ligas y acceder al listado de ligas.
// También almacena el usuario creador de cada liga en Firebase.
public class GestionarActivity extends MenuActivity {

    private EditText etNombreLiga;
    private Button btnGuardarLiga, btnVerLigas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar);

        // Referencias a los elementos de la interfaz
        etNombreLiga = findViewById(R.id.etNombreLiga);
        btnGuardarLiga = findViewById(R.id.btnGuardarLiga);
        btnVerLigas = findViewById(R.id.btnVerLigas);

        // Botón para guardar una nueva liga
        btnGuardarLiga.setOnClickListener(v -> {

            // Obtener el nombre introducido por el usuario
            String nombre = etNombreLiga.getText().toString().trim();

            // Validar que el nombre no esté vacío
            if (nombre.isEmpty()) {
                etNombreLiga.setError("Introduce un nombre");
                return;
            }

            // Generar un ID único para la liga
            String id = FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .push()
                    .getKey();

            // Obtener el ID del usuario autenticado
            String userId = FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid();

            // Crear objeto liga con su nombre y creador
            HashMap<String, Object> liga = new HashMap<>();
            liga.put("nombre", nombre);
            liga.put("creador", userId);

            // Guardar la liga en Firebase
            FirebaseDatabase.getInstance()
                    .getReference("ligas")
                    .child(id)
                    .setValue(liga)
                    .addOnCompleteListener(task -> {

                        // Comprobar si se ha guardado correctamente
                        if (task.isSuccessful()) {

                            Toast.makeText(this,
                                    "Liga guardada",
                                    Toast.LENGTH_SHORT).show();

                            // Limpiar el campo de texto
                            etNombreLiga.setText("");

                        } else {
                            Toast.makeText(this,
                                    "Error al guardar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Botón para ir a la pantalla donde se muestran las ligas
        btnVerLigas.setOnClickListener(v ->
                startActivity(new Intent(this, LigasActivity.class)));
    }
}