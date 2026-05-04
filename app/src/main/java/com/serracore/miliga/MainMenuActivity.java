package com.serracore.miliga;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends BaseActivity {

    private Button btnGestionarLigas, btnGestionarEquipos, btnGestionarPartidos,
            btnGoleadores, btnMultimedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnGestionarLigas = findViewById(R.id.btnGestionarLigas);
        btnGestionarEquipos = findViewById(R.id.btnGestionarEquipos);
        btnGestionarPartidos = findViewById(R.id.btnGestionarPartidos);
        btnGoleadores = findViewById(R.id.btnGoleadores);
        btnMultimedia = findViewById(R.id.btnMultimedia);

        btnGestionarLigas.setOnClickListener(v ->
                startActivity(new Intent(this, LigasActivity.class)));

        btnGestionarEquipos.setOnClickListener(v ->
                startActivity(new Intent(this, EquiposActivity.class)));

        btnGestionarPartidos.setOnClickListener(v ->
                startActivity(new Intent(this, ClasificacionActivity.class)));

        btnGoleadores.setOnClickListener(v ->
                startActivity(new Intent(this, GoleadoresActivity.class)));

        btnMultimedia.setOnClickListener(v ->
                startActivity(new Intent(this, MultimediaActivity.class)));
    }
}
