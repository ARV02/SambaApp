package com.example.samba.fedora;

import static com.example.samba.utils.Constants.CONNECTION_PROFILE;
import static com.example.samba.utils.Constants.PASSWORD;
import static com.example.samba.utils.Constants.USER;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samba.R;
import com.example.samba.model.SmbConnectionProfile;

public class FedoraDataFragment extends Fragment {
    private EditText usuari3, contra3, host, sharedName;
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
        host = rootView.findViewById(R.id.et_ip_address);
        sharedName = rootView.findViewById(R.id.et_shared);
        usuari3 = rootView.findViewById(R.id.usuario3);
        contra3 = rootView.findViewById(R.id.pass3);
        aceptar3 = rootView.findViewById(R.id.buttons3);
        aceptar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FedoraFilesFragment files = new FedoraFilesFragment();
                String usu = usuari3.getText().toString();
                String pass = contra3.getText().toString();
                SmbConnectionProfile smbConnectionProfile = new SmbConnectionProfile(
                        usu,
                        host.getText().toString(),
                        sharedName.getText().toString(),
                        usu
                );
                if (smbConnectionProfile.getHost().isEmpty() || smbConnectionProfile.getShareName().isEmpty() || smbConnectionProfile.getName().isEmpty() || pass.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_LONG).show();
                } else {
                    enviar3.putParcelable(CONNECTION_PROFILE, smbConnectionProfile);
                    enviar3.putString(PASSWORD, pass);
                    files.setArguments(enviar3);
                    getFragmentManager().beginTransaction().replace(R.id.container3, files).commit();
                }
            }
        });
        return rootView;
    }
}