package com.example.samba.freeBSD;

import static com.example.samba.utils.Constants.CONNECTION_PROFILE;
import static com.example.samba.utils.Constants.PASSWORD;
import static com.example.samba.utils.Constants.USER;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.samba.R;
import com.example.samba.model.SmbConnectionProfile;

public class DataFragment extends Fragment {
    private EditText usuari, contra, host, sharedName;
    private Button aceptar;

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        host = rootView.findViewById(R.id.et_free_ip_address);
        sharedName = rootView.findViewById(R.id.et_free_shared);
        usuari = rootView.findViewById(R.id.usuario);
        contra = rootView.findViewById(R.id.pass);
        aceptar = rootView.findViewById(R.id.button2);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilesFragment files = new FilesFragment();

                String username = usuari.getText().toString().trim();
                String password = contra.getText().toString();
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
                        "FreeBSD",
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
                        .replace(R.id.container, files)
                        .commit();
            }
        });
        return rootView;
    }
}