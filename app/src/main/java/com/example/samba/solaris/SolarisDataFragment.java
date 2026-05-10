package com.example.samba.solaris;

import static com.example.samba.utils.Constants.CONNECTION_PROFILE;
import static com.example.samba.utils.Constants.PASSWORD;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samba.R;
import com.example.samba.model.SmbConnectionProfile;

public class SolarisDataFragment extends Fragment {
    private EditText usuari2, contra2, host, sharedName;
    private Button aceptar2;

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
        host = rootView.findViewById(R.id.et_solaris_ip_address);
        sharedName = rootView.findViewById(R.id.et_solaris_shared);
        usuari2 = rootView.findViewById(R.id.usuario2);
        contra2 = rootView.findViewById(R.id.pass2);
        aceptar2 = rootView.findViewById(R.id.buttons2);
        aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolarisFilesFragment files = new SolarisFilesFragment();

                String username = usuari2.getText().toString().trim();
                String password = contra2.getText().toString();
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
                        "Solaris",
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
                        .replace(R.id.container2, files)
                        .commit();
            }
        });
        return rootView;
    }
}