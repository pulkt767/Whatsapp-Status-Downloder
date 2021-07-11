package com.mg.whatsappdownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class saved extends AppCompatActivity {

    ArrayList<File> list = new ArrayList<>();
    RecyclerView recyclerView;
    saved_glidAdapter saved_glidAdapter;
    File file;
    boolean chek;
    TextView exist;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.save_recycle);

        StaggeredGridLayoutManager st= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        st.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(st);

        exist = findViewById(R.id.check);


        file= new File(Environment.getExternalStorageDirectory().getPath()+"/WhatsApp Status Downloader");
        chek = check(file);

        if (chek){
            list = imageReader(file);
            saved_glidAdapter = new saved_glidAdapter(saved.this,list);
            recyclerView.setAdapter(saved_glidAdapter);
        }

    }

    private boolean check(File file) {
        if (file.exists()){
            return true;
        }
        else {
            exist.setVisibility(View.VISIBLE);
            return  false;
        }
    }

    private ArrayList<File> imageReader(File dataDirectory) {

        ArrayList<File> b = new ArrayList<File>();

        File files[] = dataDirectory.listFiles();

        if (files.length==0){
            exist.setVisibility(View.VISIBLE);
        }

        for (File singlefile : files) {
            if (singlefile.getName().endsWith(".jpg") | singlefile.getName().endsWith(".mp4")) {
                b.add(singlefile);
            }
        }
        return b;
    }

}
