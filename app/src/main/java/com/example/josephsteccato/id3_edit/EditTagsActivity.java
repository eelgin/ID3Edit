package com.example.josephsteccato.id3_edit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class EditTagsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);
        ArrayList<String> file_list = getFileNames("/Music");

    }


    // returns a list of files/directories in a directory
    private ArrayList<String> getFileNames(String path_to_get) {
        ArrayList<String> filenames = new ArrayList<String>();
        String path = Environment.getExternalStorageDirectory() + File.separator + path_to_get;

        File directory = new File(path);
        if(directory.exists()) Log.d("CONSOLE", "getFileNames: DIRECTORY EXISTS");
        else Log.d("CONSOLE", "getFileNames: DIRECTORY DOES NOT EXIST");

        File[] file_array = directory.listFiles();

        for (int i = 0; i < file_array.length; i++){
            String file_name = file_array[i].getName();
            Log.d("CONSOLE", "File-Name: "+file_name);
            filenames.add(file_name);
        }
        return filenames;
    }
}





