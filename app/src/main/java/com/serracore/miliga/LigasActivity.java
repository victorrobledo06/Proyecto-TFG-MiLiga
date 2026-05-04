package com.serracore.miliga;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class LigasActivity extends BaseActivity {

    private Button btnLigaA, btnLigaB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ligas);

        btnLigaA = findViewById(R.id.btnLigaA);
        btnLigaB = findViewById(R.id.btnLigaB);

        btnLigaA.setOnClickListener(v ->
                Toast.makeText(this, "Liga A seleccionada", Toast.LENGTH_SHORT).show());

        btnLigaB.setOnClickListener(v ->
                Toast.makeText(this, "Liga B seleccionada", Toast.LENGTH_SHORT).show());
    }
}
