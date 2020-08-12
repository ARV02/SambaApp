package com.example.samba.freeBSD;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                for (FileIdBothDirectoryInformation f : share.list(null)) {
                    Log.d("File", " " + f.getFileName());
                }
                /*String url = "smb://192.168.1.92/";
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                        null, usuario, passwd);
                SmbFile sfile = new SmbFile("smb://10.0.0.1/freebsd_compartido/", auth);
                SmbFile[] files = sfile.listFiles();
                for(int i=0; i<=files.length; i ++){
                    String fileName = files[i].getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    Log.d("Folders"," " + fileName + "\n");
                }*/
            }catch(SmbException | MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
