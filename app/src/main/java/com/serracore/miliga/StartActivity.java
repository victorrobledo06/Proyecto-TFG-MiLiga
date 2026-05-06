package com.serracore.miliga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnLogin = findViewById(R.id.btnIrLogin);
        Button btnRegistro = findViewById(R.id.btnIrRegistro);

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        btnRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));
    }
}
