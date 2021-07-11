package com.mg.whatsappdownloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class saved_glidAdapter extends RecyclerView.Adapter<saved_glidAdapter.MyViewHolder>  {

    private Context context;
    private ArrayList<File> arrayList = new ArrayList<>();

    public saved_glidAdapter(Context context, ArrayList<File> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public saved_glidAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_list_item,parent,false);
        return new saved_glidAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull saved_glidAdapter.MyViewHolder holder, final int position) {

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
                    Intent intent = new Intent(context, saved_full_image.class);
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

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.inflate(R.menu.share);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.share: Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(arrayList.get(position).toString()));
                                shareIntent.setType("image/jpg/video/mp4");
                                context.startActivity(Intent.createChooser(shareIntent,"Share Via: "));
                                break;

                            case R.id.delete:
                                File file = new File(arrayList.get(position).toString());
                                file.delete();
                                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                ((Activity)context).finish();
                                break;
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView size;
        ImageView share;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myImage);
            size = itemView.findViewById(R.id.size);
            share = itemView.findViewById(R.id.share);
        }
    }

}

