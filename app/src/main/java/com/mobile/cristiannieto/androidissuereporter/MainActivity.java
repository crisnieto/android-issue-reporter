package com.mobile.cristiannieto.androidissuereporter;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Log.d("Android Reporter","Mensaje de Prueba");
        Button test_api = (Button) findViewById(R.id.boton_test_api);
        try {
            test_api.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Context appContext = getApplicationContext();
                        EditText title = (EditText) findViewById(R.id.text_title);
                        EditText description = (EditText) findViewById(R.id.text_description);
                        IssueSender issueCreator = new IssueSender();
                        issueCreator.postApi(appContext, title.getText().toString(), description.getText().toString());
                        Toast.makeText(MainActivity.this, "TESTEANDO API!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch(NullPointerException e){
            Log.d("BOTON","Error en la interacción con el boton: " + e.toString());
        }
    }
}
