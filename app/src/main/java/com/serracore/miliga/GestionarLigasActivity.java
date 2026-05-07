package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class GestionarLigasActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_ligas);

        Button btnAnadir = findViewById(R.id.btnAnadirLiga);
        Button btnVer = findViewById(R.id.btnVerLigas);

        btnAnadir.setOnClickListener(v ->
                startActivity(new Intent(this, AnadirLigaActivity.class)));

        btnVer.setOnClickListener(v ->
                startActivity(new Intent(this, VerLigasActivity.class)));
    }
}
