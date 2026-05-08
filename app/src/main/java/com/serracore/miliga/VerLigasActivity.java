package com.serracore.miliga;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class VerLigasActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ligas);

        ListView listView = findViewById(R.id.listLigas);

        SharedPreferences prefs = getSharedPreferences("LigasDB", MODE_PRIVATE);
        Map<String, ?> todas = prefs.getAll();

        ArrayList<String> nombres = new ArrayList<>();

        for (String key : todas.keySet()) {
            if (key.startsWith("liga_")) {
                nombres.add(key.replace("liga_", ""));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nombres
        );

        listView.setAdapter(adapter);
    }
}
