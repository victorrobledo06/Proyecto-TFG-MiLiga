package com.serracore.miliga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        EditText usuario = findViewById(R.id.etNombreUsuario);
        EditText nombre = findViewById(R.id.etNombre);
        EditText apellidos = findViewById(R.id.etApellidos);
        EditText fecha = findViewById(R.id.etFecha);
        EditText correo = findViewById(R.id.etCorreo);
        EditText pass = findViewById(R.id.etPass);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> {

            String user = usuario.getText().toString().trim();
            String mail = correo.getText().toString().trim();
            String password = pass.getText().toString().trim();

            if (user.isEmpty() || mail.isEmpty() || password.isEmpty()) {
                usuario.setError(user.isEmpty() ? "Obligatorio" : null);
                correo.setError(mail.isEmpty() ? "Obligatorio" : null);
                pass.setError(password.isEmpty() ? "Obligatorio" : null);
                return;
            }

            SharedPreferences prefs = getSharedPreferences("MiLigaPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("usuario", user)
                    .putString("correo", mail)
                    .putString("password", password)
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
