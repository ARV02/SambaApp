package com.example.samba.fedora;

import static com.example.samba.utils.Constants.CONNECTION_PROFILE;
import static com.example.samba.utils.Constants.PASSWORD;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.samba.R;
import com.example.samba.data.smb.SmbFileRepositoryImpl;
import com.example.samba.model.SmbConnectionProfile;
import com.example.samba.domain.model.SmbFileItem;
import com.example.samba.domain.model.SmbFileResult;
import com.example.samba.domain.repository.SmbFileRepository;

import java.util.ArrayList;
import java.util.List;

public class FedoraFilesFragment extends Fragment {

    private String passwd;
    private SmbConnectionProfile connectionProfile;
    private ListView listView;

    private final SmbFileRepository repository = new SmbFileRepositoryImpl();

    public FedoraFilesFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static FedoraFilesFragment newInstance(String param1, String param2) {
        FedoraFilesFragment fragment = new FedoraFilesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_fedora_files, container, false);
        listView = rootView.findViewById(R.id.file2);
        new FedoraFilesFragment.ListFilesTask().execute();
        return rootView;
    }

    private class ListFilesTask extends AsyncTask<Void, Void, SmbFileResult<List<SmbFileItem>>> {
        @Override
        protected SmbFileResult<List<SmbFileItem>> doInBackground(Void... voids) {
            return repository.listFiles(connectionProfile, passwd);
        }

        @Override
        protected void onPostExecute(SmbFileResult<List<SmbFileItem>> listSmbFileResult) {
            if (listSmbFileResult instanceof SmbFileResult.Success) {
                List<SmbFileItem> files =
                        ((SmbFileResult.Success<List<SmbFileItem>>) listSmbFileResult).getData();

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
                return;
            }

            if (listSmbFileResult instanceof SmbFileResult.Error) {
                String message = ((SmbFileResult.Error) listSmbFileResult).getMessage();

                Toast.makeText(
                        requireContext(),
                        message,
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}