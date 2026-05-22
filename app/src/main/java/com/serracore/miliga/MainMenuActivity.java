package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

// Activity que actúa como menú principal de la aplicación.
// Desde aquí el usuario puede acceder a las principales funcionalidades.
public class MainMenuActivity extends MenuActivity {

    private Button btnGestionarLigas, btnVerLigas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Referencias a los botones del menú
        btnGestionarLigas = findViewById(R.id.btnGestionarLigas);
        btnVerLigas = findViewById(R.id.btnVerLigas);

        // Botón para acceder a la gestión de ligas (crear ligas)
        if (btnGestionarLigas != null) {
            btnGestionarLigas.setOnClickListener(v ->
                    startActivity(new Intent(this, GestionarActivity.class)));
        }

        // Botón para ver las ligas existentes
        if (btnVerLigas != null) {
            btnVerLigas.setOnClickListener(v ->
                    startActivity(new Intent(this, LigasActivity.class)));
        }
    }
}