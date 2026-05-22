package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

// Activity encargada de registrar nuevos usuarios en la aplicación.
// Utiliza Firebase Authentication para crear cuentas con email y contraseña.
public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Referencias a los campos de entrada
        EditText correo = findViewById(R.id.etCorreo);
        EditText pass = findViewById(R.id.etPass);

        // Botón de registro
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Evento al pulsar el botón de registrar
        btnRegistrar.setOnClickListener(v -> {

            String email = correo.getText().toString().trim();
            String password = pass.getText().toString().trim();

            // Validar que los campos obligatorios no estén vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear usuario en Firebase con email y contraseña
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            // Registro correcto
                            Toast.makeText(this, "Registro correcto", Toast.LENGTH_SHORT).show();

                            // Volver a la pantalla de login pasando el email
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);

                            // Cerrar esta activity
                            finish();

                        } else {

                            // Mostrar error si el registro falla
                            Toast.makeText(this,
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}