package com.mg.whatsappdownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<File> list = new ArrayList<>();
    RecyclerView recyclerView;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    glidAdapter glidAdapter;
    File file, storagefile;
    boolean check;
    TextView exist;
    File fi[]=null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView= findViewById(R.id.recycle);
        exist = findViewById(R.id.check);

        StaggeredGridLayoutManager st= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(st);

        file= new File(Environment.getExternalStorageDirectory().getPath()+"/WhatsApp/Media/.Statuses");
        storagefile= new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp Status Downloader");

        check = check(file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                String[] permission = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_STORAGE_CODE);

            }
            else {
                if (check){
                    list = imageReader(file);
                    glidAdapter = new glidAdapter(MainActivity.this,list);
                    recyclerView.setAdapter(glidAdapter);
                }

            }
        }
        else {
            if (check){
                list = imageReader(file);
                glidAdapter = new glidAdapter(MainActivity.this,list);
                recyclerView.setAdapter(glidAdapter);
            }

        }

    }

    private boolean check(File file) {
        if(file.exists()){
            return true;
        }
        else {
            exist.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.saved: Intent intent = new Intent(MainActivity.this,saved.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<File> imageReader(File dataDirectory) {

        ArrayList<File> b = new ArrayList<File>();

        File files[] = dataDirectory.listFiles();

        if (!storagefile.exists()){
            storagefile.mkdirs();
        }

        fi= storagefile.listFiles();

        for (File singlefile : files) {

            int y=0;
            String path= singlefile.getPath();

            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                path = path.substring(cut + 1);
            }

            if (singlefile.getName().endsWith(".jpg") | singlefile.getName().endsWith(".mp4")) {
                if (fi.length==0){

                }
                else {
                    for (File filex : fi){
                        if (filex.getName().matches(path)){
                            y=1;
                        }
                    }
                }

                if (y==0){
                    b.add(singlefile);
                }
            }
        }
        return b;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (check){
                        list = imageReader(file);
                        glidAdapter = new glidAdapter(MainActivity.this,list);
                        recyclerView.setAdapter(glidAdapter);
                    }
                }
                else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
