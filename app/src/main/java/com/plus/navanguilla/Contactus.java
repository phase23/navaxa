package com.plus.navanguilla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Contactus extends AppCompatActivity {

    Button backmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        backmain = (Button)findViewById(R.id.backtomain);


        backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contactus.this, Myactivity.class);
                startActivity(intent);

            }
        });

    }
}