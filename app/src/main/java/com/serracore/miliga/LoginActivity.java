package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

// Activity encargada de gestionar el inicio de sesión del usuario.
// Utiliza Firebase Authentication para validar las credenciales.
public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private Button btnIniciarSesion;

    // Instancia de FirebaseAuth para gestionar autenticación
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias a los elementos de la interfaz
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Recibir email desde la pantalla de registro (si existe)
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            etUsuario.setText(email);
        }

        // Evento al pulsar el botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(v -> {

            String user = etUsuario.getText().toString().trim();
            String pass = etContrasena.getText().toString().trim();

            // Validación de campos vacíos
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Autenticación con Firebase
            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            // Login correcto
                            Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();

                            // Navegar al menú principal
                            startActivity(new Intent(this, MainMenuActivity.class));

                            // Cerrar esta activity para que no se pueda volver atrás
                            finish();

                        } else {
                            // Mostrar error en caso de fallo en autenticación
                            Toast.makeText(this,
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}