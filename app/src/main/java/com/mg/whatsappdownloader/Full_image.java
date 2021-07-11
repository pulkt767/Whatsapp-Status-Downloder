package com.mg.whatsappdownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Full_image extends AppCompatActivity {

    ImageView close, full_image;
    FloatingActionButton save;
    String path;

    OutputStream ou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        close = findViewById(R.id.close);
        full_image = findViewById(R.id.full_image);
        save = findViewById(R.id.save);

        getSupportActionBar().hide();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String Image_data = getIntent().getExtras().getString("img");
        path= Uri.parse(Image_data).getPath();
        full_image.setImageURI(Uri.parse(Image_data));

        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            path = path.substring(cut + 1);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) full_image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                File filepath = Environment.getExternalStorageDirectory();
                File dir= new File(filepath.getAbsolutePath() + "/WhatsApp Status Downloader");
                dir.mkdir();
                File file = new File(dir, path);

                try {
                    ou = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ou);
                Toast.makeText(Full_image.this,"Download Successful", Toast.LENGTH_SHORT).show();

                try {
                    ou.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    ou.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
