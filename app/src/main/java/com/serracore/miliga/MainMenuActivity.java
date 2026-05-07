package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainMenuActivity extends MenuActivity {

    private Button btnGestionarLigas, btnVerLigas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnGestionarLigas = findViewById(R.id.btnGestionarLigas);
        btnVerLigas = findViewById(R.id.btnVerLigas);

        // Crear ligas
        btnGestionarLigas.setOnClickListener(v ->
                startActivity(new Intent(this, GestionarActivity.class)));

        // Ver ligas
        btnVerLigas.setOnClickListener(v ->
                startActivity(new Intent(this, LigasActivity.class)));
    }
}
