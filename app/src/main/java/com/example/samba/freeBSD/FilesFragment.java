package com.example.samba.freeBSD;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.samba.R;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbSession;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String usuario;
    private String passwd;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Object> list;

    // TODO: Rename and change types of parameters

    public FilesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FilesFragment newInstance(String param1, String param2) {
        FilesFragment fragment = new FilesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments().getString("usr");
            passwd = getArguments().getString("passwd");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);
        listView = rootView.findViewById(R.id.files);
        Log.d("Recivido", " " + usuario);
        Log.d("Recivido", " " + passwd);
        new SmbaFiles().execute();
        return rootView;
    }

    private class SmbaFiles extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SMBClient client = new SMBClient(SmbConfig.createDefaultConfig());
                Connection c = client.connect("10.0.0.1");
                Session s = c.authenticate(new AuthenticationContext(usuario, passwd.toCharArray(), ""));
                DiskShare share = (DiskShare) s.connectShare("freebsd_compartido");
                list = new ArrayList<>();
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, list);
                for (FileIdBothDirectoryInformation f : share.list(null)) {
                    Log.d("File", " " + f.getFileName());
                    list.add(f.getFileName());
                }
                Log.d("Array", " " + list);
            }catch(SmbException | MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.setAdapter(adapter);
            Log.d("List", " " + listView);
        }
    }
}
