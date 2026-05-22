package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

// Activity inicial de la aplicación.
// Actúa como pantalla de bienvenida desde la que el usuario puede
// elegir entre iniciar sesión o registrarse.
public class StartActivity extends AppCompatActivity {

    // Instancia de Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Cargar layout
        setContentView(R.layout.activity_start);

        // Referencias a los botones
        Button btnLogin = findViewById(R.id.btnIrLogin);
        Button btnRegistro = findViewById(R.id.btnIrRegistro);

        // Botón que lleva a la pantalla de login
        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        // Botón que lleva a la pantalla de registro
        btnRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));
    }
}
