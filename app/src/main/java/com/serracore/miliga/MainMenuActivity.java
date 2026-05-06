package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnGestionarLigas = findViewById(R.id.btnGestionarLigas);

        btnGestionarLigas.setOnClickListener(v ->
                startActivity(new Intent(this, GestionarLigasActivity.class)));
    }
}
