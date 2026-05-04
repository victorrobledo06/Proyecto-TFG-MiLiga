package com.serracore.miliga;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EquiposActivity extends BaseActivity {

    private ListView listEquipos;
    private Button btnAnadirLiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos);

        listEquipos = findViewById(R.id.listEquipos);
        btnAnadirLiga = findViewById(R.id.btnAnadirLiga);

        String[] equipos = {"Atléticos FC", "Universidad CF", "Tigers FC"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                equipos
        );
        listEquipos.setAdapter(adapter);

        btnAnadirLiga.setOnClickListener(v ->
                Toast.makeText(this, "Añadir Liga pulsado", Toast.LENGTH_SHORT).show());
    }
}
