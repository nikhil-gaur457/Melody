package gaur.nikhil.melody;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items; // to store song items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise our listView.
        listView = findViewById(R.id.listViewSong);

        runtimePermission();
    }

    public void runtimePermission() { // Method to give permission to storage.
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong (File file)   { // Method to store songs in a list by file.
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        // Check for file is mp3 file or a folder.
        for (File singlefile: files) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) { // check file is single or in a directory.
                arrayList.addAll(findSong(singlefile));
            } else { // check file is mp3 file or wav file.
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void displaySongs() { // Method to display songs listView.
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) { // Add songs to items list.
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "");
        }
        /*ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(myAdapter);*/

        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);
    }

    class customAdapter extends BaseAdapter { // Custom Adapter.

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textSong = myView.findViewById(R.id.txtsongname);
            textSong.setSelected(true);
            textSong.setText(items[i]);
            return myView;
        }
    }
}