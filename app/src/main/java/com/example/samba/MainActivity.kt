package com.example.samba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.samba.fedora.FedoraActivity;
import com.example.samba.freeBSD.FreeBSD;
import com.example.samba.solaris.SolarisActivity;

public class MainActivity extends AppCompatActivity {
    private Switch FreeBSD,Solaris, Fedora;
    private Button acceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Instances();
        Validation();
    }

    private void Instances(){
        FreeBSD = findViewById(R.id.switch1);
        Solaris = findViewById(R.id.switch2);
        Fedora = findViewById(R.id.switch3);
        acceder = findViewById(R.id.button);
    }

    private void Validation() {
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FreeBSD.isChecked() && Solaris.isChecked() && Fedora.isChecked()){
                        Toast.makeText(MainActivity.this, "Solo se puede seleccionar una opcion", Toast.LENGTH_LONG)
                            .show();
                }else if (FreeBSD.isChecked()){
                    Intent i = new Intent(view.getContext(), FreeBSD.class);
                    startActivity(i);
                }else if(Solaris.isChecked()){
                    Intent intent = new Intent(view.getContext(), SolarisActivity.class);
                    startActivity(intent);
                }else if(Fedora.isChecked()){
                    Intent i2 = new Intent(view.getContext(), FedoraActivity.class);
                    startActivity(i2);
                }
            }
        });
    }
}