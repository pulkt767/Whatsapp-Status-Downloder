package com.mg.whatsappdownloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

public class glidAdapter extends RecyclerView.Adapter<glidAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<File> arrayList = new ArrayList<>();
    private String path;
    OutputStream ou;
    String title;
    MainActivity mainActivity;


    public glidAdapter(Context context, ArrayList<File> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public glidAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final glidAdapter.MyViewHolder holder, final int position) {
        File f = new File(arrayList.get(position).getPath());
        double size = f.length();
        holder.size.setText(String.format(Locale.US,"%.2f KB", size/1000));
        if (arrayList.get(position).getName().endsWith(".jpg")){
            holder.imageView.setImageURI(Uri.parse(arrayList.get(position).toString()));
        }
        else {
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(arrayList.get(position).getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            holder.imageView.setImageBitmap(thumbnail);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(position).getName().endsWith(".jpg")){
                    Intent intent = new Intent(context, Full_image.class);
                    intent.putExtra("img",arrayList.get(position).toString());
                    context.startActivity(intent);
                }

                else {
                    Intent intent = new Intent(context, Full_video.class);
                    intent.putExtra("video",arrayList.get(position).toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                path= Uri.parse(arrayList.get(position).toString()).getPath();

                int cut = path.lastIndexOf('/');
                if (cut != -1) {
                    path = path.substring(cut + 1);
                }

                title = path;
                if(arrayList.get(position).getName().endsWith(".jpg")){
                    holder.image.setImageURI(Uri.parse(arrayList.get(position).toString()));
                    BitmapDrawable drawable = (BitmapDrawable) holder.image.getDrawable();
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
                    Toast.makeText(context,"Download Successful", Toast.LENGTH_SHORT).show();

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

                else{
                    File filepath = Environment.getExternalStorageDirectory();
                    File dir= new File(filepath.getAbsolutePath() + "/WhatsApp Status Downloader");

                    try {
                        exportFile(arrayList.get(position), dir,path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context,"Download Successful", Toast.LENGTH_SHORT).show();

                }

            }

        });
    }

    private File exportFile(File src, File dst,String path) throws IOException {

        //if folder does not exist
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }

        File expFile = new File(dst.getPath() + File.separator + path);
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            assert inChannel != null;
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

        return expFile;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView size;
        FloatingActionButton download;
        ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myImage);
            size = itemView.findViewById(R.id.size);
            download = itemView.findViewById(R.id.download);
            image = itemView.findViewById(R.id.imagex);

        }
    }



}
