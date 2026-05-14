package com.example.samba.freeBSD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.samba.R;
import com.example.samba.fedora.FedoraFilesFragment;
import com.example.samba.model.SmbConnectionProfile;
import com.example.samba.presentation.connection.ConnectionFormUiState;
import com.example.samba.presentation.connection.ConnectionFormViewModel;
import com.example.samba.utils.SmbBundleFactory;

public class DataFragment extends Fragment {
    private EditText usuari, contra, host, sharedName;
    private Button aceptar;

    private ConnectionFormViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(ConnectionFormViewModel.class);
        observeConnectionFormState();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validateAndCreateProfile(
                        "FreeBSD",
                        host.getText().toString(),
                        sharedName.getText().toString(),
                        usuari.getText().toString(),
                        contra.getText().toString()
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
        FilesFragment files = new FilesFragment();

        Bundle bundle = SmbBundleFactory.createConnectionBundle(
                connectionProfile,
                password
        );
        files.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, files)
                .commit();
    }
}