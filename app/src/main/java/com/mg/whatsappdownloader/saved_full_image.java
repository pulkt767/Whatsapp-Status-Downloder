package com.mg.whatsappdownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class saved_full_image extends AppCompatActivity {

    ImageView close, full_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_full_image);

        close = findViewById(R.id.close);
        full_image = findViewById(R.id.full_image);

        getSupportActionBar().hide();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String Image_data = getIntent().getExtras().getString("img");
        full_image.setImageURI(Uri.parse(Image_data));

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
