package com.serracore.miliga;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.serracore.miliga.MainMenuActivity;
import com.serracore.miliga.R;

public class LoginActivity extends BaseActivity {

    private EditText etUsuario, etContrasena;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = etUsuario.getText().toString().trim();
                String pass = etContrasena.getText().toString().trim();

                // Aquí puedes poner tu lógica real de login
                if (user.equals("admin") && pass.equals("1234")) {
                    Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
