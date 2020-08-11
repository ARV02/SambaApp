package com.example.samba.freeBSD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.samba.R;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class DataFragment extends Fragment {
    private EditText usuario, contra;
    private Button aceptar;
    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        usuario = rootView.findViewById(R.id.usuario);
        contra = rootView.findViewById(R.id.pass);
        aceptar = rootView.findViewById(R.id.button2);
        final String usu = usuario.getText().toString();
        final String pass = contra.getText().toString();
        String sharedFolder="";
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = "smb://10.0.0.1/";
                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                            null, usu, pass);
                    SmbFile sfile = new SmbFile(url, auth);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Error: " + e, Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }
}