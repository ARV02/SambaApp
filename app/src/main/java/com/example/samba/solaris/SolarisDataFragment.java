package com.example.samba.solaris;

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
 * Use the {@link SolarisDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolarisDataFragment extends Fragment {
    private EditText usuari2, contra2;
    private Button aceptar2;
    private Bundle enviar2 = new Bundle();

    // TODO: Rename parameter arguments, choose names that match

    public SolarisDataFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static SolarisDataFragment newInstance(String param1, String param2) {
        SolarisDataFragment fragment = new SolarisDataFragment();
        Bundle args = new Bundle();
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
        View rootView = inflater.inflate(R.layout.fragment_solaris_data, container, false);
        usuari2 = rootView.findViewById(R.id.usuario2);
        contra2 = rootView.findViewById(R.id.pass2);
        aceptar2 = rootView.findViewById(R.id.buttons2);
        aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolarisFilesFragment files = new SolarisFilesFragment();
                String usu = usuari2.getText().toString();
                String pass = contra2.getText().toString();
                enviar2.putString("usrs", usu);
                enviar2.putString("passwds", pass);
                files.setArguments(enviar2);
                getFragmentManager().beginTransaction().replace(R.id.container2, files).commit();
            }
        });
        return rootView;
    }
}