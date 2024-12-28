package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Nointernet extends AppCompatActivity {
    Button trynet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);

        trynet = (Button)findViewById(R.id.trynet);

        trynet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nointernet.this, MainActivity.class);
                startActivity(intent);

            }
        });





    }
}