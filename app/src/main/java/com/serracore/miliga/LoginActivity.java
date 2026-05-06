package com.serracore.miliga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsuario = findViewById(R.id.etUsuario);
        EditText etContrasena = findViewById(R.id.etContrasena);
        Button btnIniciar = findViewById(R.id.btnIniciarSesion);

        btnIniciar.setOnClickListener(v -> {

            String mail = etUsuario.getText().toString().trim();
            String password = etContrasena.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("MiLigaPrefs", MODE_PRIVATE);
            String mailGuardado = prefs.getString("correo", "");
            String passGuardada = prefs.getString("password", "");

            if (mail.equals(mailGuardado) && password.equals(passGuardada)) {
                startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                finish();
            } else {
                etUsuario.setError("Correo o contraseña incorrectos");
                etContrasena.setError("Correo o contraseña incorrectos");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MiLigaPrefs", MODE_PRIVATE);
        String mailGuardado = prefs.getString("correo", "");

        EditText etUsuario = findViewById(R.id.etUsuario);
        etUsuario.setText(mailGuardado);
    }
}
