package com.example.samba.freeBSD;

import static com.example.samba.utils.Constants.CONNECTION_PROFILE;
import static com.example.samba.utils.Constants.PASSWORD;
import static com.example.samba.utils.Constants.USER;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.samba.R;
import com.example.samba.model.SmbConnectionProfile;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.util.ArrayList;

public class FilesFragment extends Fragment {

    private String passwd;
    private SmbConnectionProfile connectionProfile;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Object> list;

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
        new SmbaFiles().execute();
        return rootView;
    }

    private class SmbaFiles extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SMBClient client = new SMBClient(SmbConfig.createDefaultConfig());

                Connection c = client.connect(connectionProfile.getHost());
                Session s = c.authenticate(new AuthenticationContext(connectionProfile.getUsername(), passwd.toCharArray(), ""));

                DiskShare share = (DiskShare) s.connectShare(connectionProfile.getShareName());
                list = new ArrayList<>();
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, list);
                for (FileIdBothDirectoryInformation f : share.list(null)) {
                    list.add(f.getFileName());
                }
            } catch (SMBApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.setAdapter(adapter);
        }
    }
}

