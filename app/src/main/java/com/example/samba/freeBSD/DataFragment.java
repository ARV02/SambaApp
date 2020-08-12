package com.example.samba.freeBSD;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.samba.R;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class DataFragment extends Fragment {
    private EditText usuari, contra;
    private Button aceptar;
    private Bundle enviar = new Bundle();

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        usuari = rootView.findViewById(R.id.usuario);
        contra = rootView.findViewById(R.id.pass);
        aceptar = rootView.findViewById(R.id.button2);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilesFragment files = new FilesFragment();
                String usu = usuari.getText().toString();
                String pass = contra.getText().toString();
                enviar.putString("usr", usu);
                enviar.putString("passwd", pass);
                files.setArguments(enviar);
                getFragmentManager().beginTransaction().replace(R.id.container, files).commit();
            }
        });
        return rootView;
    }
}