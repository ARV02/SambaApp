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

                String username = usuari3.getText().toString().trim();
                String password = contra3.getText().toString();
                String hostValue = host.getText().toString().trim();
                String shareNameValue = sharedName.getText().toString().trim();

                if (username.isEmpty()
                        || password.isEmpty()
                        || hostValue.isEmpty()
                        || shareNameValue.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                SmbConnectionProfile connectionProfile = new SmbConnectionProfile(
                        "Fedora",
                        hostValue,
                        shareNameValue,
                        username
                );

                Bundle bundle = new Bundle();
                bundle.putParcelable(CONNECTION_PROFILE, connectionProfile);
                bundle.putString(PASSWORD, password);

                files.setArguments(bundle);

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container3, files)
                        .commit();
            }
        });
        return rootView;
    }
}