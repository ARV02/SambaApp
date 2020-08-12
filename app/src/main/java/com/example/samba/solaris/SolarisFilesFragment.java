package com.example.samba.solaris;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samba.R;
import com.example.samba.freeBSD.FilesFragment;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.net.MalformedURLException;

import jcifs.smb.SmbException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolarisFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolarisFilesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String usuario;
    private String passwd;

    public SolarisFilesFragment() {
        // Required empty public constructor
    }

    public static SolarisFilesFragment newInstance(String param1, String param2) {
        SolarisFilesFragment fragment = new SolarisFilesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments().getString("usrs");
            passwd = getArguments().getString("passwds");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_solaris_files, container, false);
        Log.d("Recivido", " " + usuario);
        Log.d("Recivido", " " + passwd);
        new SolarisFilesFragment.SmbaFiles().execute();
        return rootView;
    }

    private class SmbaFiles extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SMBClient client = new SMBClient(SmbConfig.createDefaultConfig());
                Connection c = client.connect("10.0.0.5");
                Session s = c.authenticate(new AuthenticationContext(usuario, passwd.toCharArray(), ""));
                DiskShare share = (DiskShare) s.connectShare("compartido_solaris");
                for (FileIdBothDirectoryInformation f : share.list(null)) {
                    Log.d("File", " " + f.getFileName());
                }
            }catch(SmbException | MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}