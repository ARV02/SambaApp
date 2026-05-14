package com.example.samba.fedora;

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
import com.example.samba.utils.SmbBundleFactory;

public class FedoraDataFragment extends Fragment {
    private EditText usuari3, contra3, host, sharedName;
    private Button aceptar3;

    private ConnectionFormViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(ConnectionFormViewModel.class);
        observeConnectionFormState();

        aceptar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validateAndCreateProfile(
                        "Fedora",
                        host.getText().toString(),
                        sharedName.getText().toString(),
                        usuari3.getText().toString(),
                        contra3.getText().toString()
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
        FedoraFilesFragment files = new FedoraFilesFragment();

        Bundle bundle = SmbBundleFactory.createConnectionBundle(
                connectionProfile,
                password
        );
        files.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container3, files)
                .commit();
    }
}