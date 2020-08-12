package com.example.samba.fedora;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.samba.R;
import com.example.samba.freeBSD.FilesFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FedoraDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FedoraDataFragment extends Fragment {
    private EditText usuari3, contra3;
    private Button aceptar3;
    private Bundle enviar3 = new Bundle();
    public FedoraDataFragment() {
        // Required empty public constructor
    }

    public static FedoraDataFragment newInstance(String param1, String param2) {
        FedoraDataFragment fragment = new FedoraDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fedora_data, container, false);
        usuari3 = rootView.findViewById(R.id.usuario3);
        contra3 = rootView.findViewById(R.id.pass3);
        aceptar3 = rootView.findViewById(R.id.buttons3);
        aceptar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FedoraFilesFragment files = new FedoraFilesFragment();
                String usu = usuari3.getText().toString();
                String pass = contra3.getText().toString();
                enviar3.putString("usrf", usu);
                enviar3.putString("passwdf", pass);
                files.setArguments(enviar3);
                getFragmentManager().beginTransaction().replace(R.id.container3, files).commit();
            }
        });
        return rootView;
    }
}