package com.example.josephsteccato.id3_edit;

// RecyclerView.Adapter
// RecyclerView.ViewHolder

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.MyFileViewHolder>{



    interface OnFileCheckListener {
        void onFileCheck (MyFile file);
        void onFileUncheck (MyFile file);
    }

    private Context mCtx;
    private List<MyFile> fileList;



    @NonNull
    private OnFileCheckListener onFileCheckListener;

    public MyFileAdapter(Context mCtx, List<MyFile> fileList, @NonNull OnFileCheckListener onFileCheckListener) {

        this.mCtx = mCtx;
        this.fileList = fileList;
        this.onFileCheckListener = onFileCheckListener;


    }


    @NonNull
    @Override
    public MyFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_layout,null);
        return new MyFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyFileViewHolder holder, final int position) {
        final MyFile file = fileList.get(position);
        holder.textViewFileName.setText(file.getName());
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(file.getIcon(),null));
        holder.textViewSize.setText(file.getSize());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCtx instanceof SelectFilesActivity){
                    if(fileList.get(position).getThisFile().isDirectory()){
                        ((SelectFilesActivity)mCtx).openPath(fileList.get(position).getName() + File.separator);
                    }
                    else{
                        Toast.makeText(mCtx, fileList.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(mCtx, fileList.get(position).getName(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        holder.selectBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.selectBox.isChecked()){
                    onFileCheckListener.onFileCheck(file);

                }
                else{
                    onFileCheckListener.onFileUncheck(file);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class MyFileViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewFileName;
        TextView textViewSize;
        CheckBox selectBox;


        public MyFileViewHolder(final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewFileName = itemView.findViewById(R.id.textViewFileName);
            textViewSize = itemView.findViewById(R.id.textViewSize);
            selectBox = itemView.findViewById(R.id.selectFileBox);


        }






    }

}
