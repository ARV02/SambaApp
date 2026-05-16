package com.example.samba.freeBSD;

import static com.example.samba.utils.Constants.CONNECTION_PROFILE;
import static com.example.samba.utils.Constants.PASSWORD;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.samba.R;
import com.example.samba.model.SmbConnectionProfile;
import com.example.samba.domain.model.SmbFileItem;
import com.example.samba.presentation.filebrowser.FileBrowserUiState;
import com.example.samba.presentation.filebrowser.FileBrowserViewModel;

import java.util.ArrayList;
import java.util.List;

/*public class FilesFragment extends Fragment {

    private String passwd;
    private SmbConnectionProfile connectionProfile;
    private ListView listView;

    private FileBrowserViewModel viewModel;

    public FilesFragment() {
        // Required empty public constructor
    }

    public static FilesFragment newInstance(String param1, String param2) {
        FilesFragment fragment = new FilesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            connectionProfile = getArguments().getParcelable(CONNECTION_PROFILE);
            passwd = getArguments().getString(PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);
        listView = rootView.findViewById(R.id.files);
        viewModel = new ViewModelProvider(this).get(FileBrowserViewModel.class);
        observeFileBrowserState();
        viewModel.listFiles(connectionProfile, passwd);
        return rootView;
    }

    private void observeFileBrowserState() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof FileBrowserUiState.Loading) {
                Toast.makeText(requireContext(), "Loading files...", Toast.LENGTH_SHORT).show();
                return;
            }

            if (state instanceof FileBrowserUiState.Success) {
                FileBrowserUiState.Success success = (FileBrowserUiState.Success) state;
                showFiles(success.getFiles());
                return;
            }

            if (state instanceof FileBrowserUiState.Error) {
                FileBrowserUiState.Error error = (FileBrowserUiState.Error) state;
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showFiles(List<SmbFileItem> files) {
        List<String> fileNames = new ArrayList<>();

        for (SmbFileItem file : files) {
            fileNames.add(file.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                fileNames
        );

        listView.setAdapter(adapter);
    }
}*/
