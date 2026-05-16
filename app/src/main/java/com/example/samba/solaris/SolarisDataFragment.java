package com.example.samba.solaris;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.samba.R;
import com.example.samba.model.SmbConnectionProfile;
import com.example.samba.presentation.connection.ConnectionFormUiState;
import com.example.samba.presentation.connection.ConnectionFormViewModel;
import com.example.samba.presentation.filebrowser.FileBrowserComposeFragment;
import com.example.samba.utils.SmbBundleFactory;

public class SolarisDataFragment extends Fragment {
    private EditText usuari2, contra2, host, sharedName;
    private Button aceptar2;

    private ConnectionFormViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(ConnectionFormViewModel.class);
        observeConnectionFormState();

        aceptar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validateAndCreateProfile(
                        "Solaris",
                        host.getText().toString(),
                        sharedName.getText().toString(),
                        usuari2.getText().toString(),
                        contra2.getText().toString()
                );
            }
        });
        return rootView;
    }

    private void observeConnectionFormState() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {

            if (state instanceof ConnectionFormUiState.Success) {
                ConnectionFormUiState.Success success = (ConnectionFormUiState.Success) state;

                navigateToFiles(
                        success.getConnectionProfile(),
                        success.getPassword()
                );
                viewModel.resetState();
            }

            if (state instanceof ConnectionFormUiState.ValidationError) {
                ConnectionFormUiState.ValidationError error =
                        (ConnectionFormUiState.ValidationError) state;
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                viewModel.resetState();
            }
        });
    }

    private void navigateToFiles(
            SmbConnectionProfile connectionProfile,
            String password
    ) {
        FileBrowserComposeFragment files = new FileBrowserComposeFragment();

        Bundle bundle = SmbBundleFactory.createConnectionBundle(
                connectionProfile,
                password
        );
        files.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container2, files)
                .commit();
    }
}