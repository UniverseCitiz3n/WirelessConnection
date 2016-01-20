package horbacz.maciej.wirelessconnection;

import android.app.ListActivity;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Explorer extends ListActivity {

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        path = "/";
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);


        List<String> values = new ArrayList<String>();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".") & !file.endsWith("rc")) {
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_2, android.R.id.text1, values);
        setListAdapter(adapter);

        ListView lv= getListView();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {

                String filename=getFilename(pos);
                setRingtone(filename);
                Toast.makeText(getApplicationContext(), "Ringtone changed!",
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        String filename= getFilename(pos);
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(this, Explorer.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {

            if (filename.endsWith(".jpg") |
                    filename.endsWith(".jpeg") |
                    filename.endsWith(".png") |
                    filename.endsWith(".bmp"))
                setWallpaper(filename);
            else if (filename.endsWith(".mp3"))
                playTrack(filename);
            else
                openRest(filename);
        }
    }

    private String getFilename(int position){
        String filename = (String) getListAdapter().getItem(position);
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        return filename;
    }

    public void setWallpaper(String path){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+path), "image/*");
        startActivity(intent);
    }

    public void setRingtone(String path){
        File ring = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, ring.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, ring.getName());
        values.put(MediaStore.MediaColumns.SIZE, ring.length());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "Variuous");
        values.put(MediaStore.Audio.Media.DURATION, 120);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        //Insert it into the database
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ring.getAbsolutePath());
        Uri newUri = this.getContentResolver().insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(
                this,
                RingtoneManager.TYPE_RINGTONE,
                newUri
        );
    }

    public void playTrack(String path){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+path), "audio/*");
        startActivity(intent);
    }

    public void openRest(String path){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+path), "application/*");
        startActivity(intent);
    }

}