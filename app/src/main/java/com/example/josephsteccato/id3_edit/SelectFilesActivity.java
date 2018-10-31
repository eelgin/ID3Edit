package com.example.josephsteccato.id3_edit;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SelectFilesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyFileAdapter adapter;
    String currentPath;

    Set<String> extensions = new HashSet<String>();

    ArrayList<MyFile> myFileList;
    private ArrayList<MyFile> currentSelectedItems;

    //
    // onCreate
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_files);

        extensions.add(".mp3");
        extensions.add(".m4a");
        extensions.add(".wav");
        extensions.add(".ogg");
        extensions.add(".flac");


        myFileList = new ArrayList<MyFile>();
        currentSelectedItems = new ArrayList<MyFile>();

        // create recyclerView for viewing files in CardViews
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set current path equal to default "Music" folder and open
        currentPath = Environment.getExternalStorageDirectory() + File.separator + "Music" + File.separator;
        initializeFiles();
    }


    // override back-button function
    @Override
    public void onBackPressed(){
        previousList();
    }

    //
    // openTagEditor
    //      Bundle filenames and working directory to pass to next activity
    //
    public void openTagEditor(View view){

        ArrayList<String> fileNames = new ArrayList<>();
        int i=0;
        for (MyFile file:currentSelectedItems)
            fileNames.add(file.getName());

        Intent intent = new Intent(this, EditTagsActivity.class);
        Bundle extras = new Bundle();
        extras.putStringArrayList("FILE_NAMES",fileNames);
        extras.putString("DIRECTORY",currentPath);
        intent.putExtras(extras);
        startActivity(intent);
    }

    //
    // initializeFiles:
    //      Pass a directory path to a string and view the contents in the RecyclerView
    //
    protected void initializeFiles(){
        getFileNames();
        adapter = new MyFileAdapter(this, myFileList, new MyFileAdapter.OnFileCheckListener() {
            @Override
            public void onFileCheck(MyFile file) {
                currentSelectedItems.add(file);
                Log.d("CONSOLE:","File checked: "+ file.getName());
            }
            @Override
            public void onFileUncheck(MyFile file) {
                currentSelectedItems.remove(file);
                Log.d("CONSOLE:","File unchecked: "+ file.getName());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    //
    // openPath:
    //      pass a directory path to a string and view the contents in the RecyclerView
    //
    protected void openPath(String path){

        currentPath = currentPath.concat(path);
        myFileList.clear();
        getFileNames();
        adapter.notifyDataSetChanged();
    }

    //
    // previousList:
    //       navigate to the parent directory
    //
    protected void previousList(){
        if(!currentPath.equals(Environment.getExternalStorageDirectory() + File.separator)) {
            String temp = currentPath.substring(0,currentPath.length()-2);
            currentPath = temp.substring(0,temp.lastIndexOf("/")).concat(File.separator);

            myFileList.clear();
            getFileNames();
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(this, "Directory Limit Reached", Toast.LENGTH_SHORT).show();
        }
    }

    //
    // getFileNames:
    //       returns a list of files/directories in a directory
    //
    private void getFileNames() {
        File directory = new File(currentPath);
        File[] file_array = directory.listFiles();

        for (File file:file_array){
            String name = file.getName();

            // add directories
            if (file.isDirectory())
            {
                myFileList.add(new MyFile(file));
                Log.d("getFileNames()","DIRECTORY: "+name);
            }
            // add only audio files
            else if (extensions.contains(getFileExtension(name))){
                Log.d("getFileNames()","MP3_DETECTED: "+name);
                myFileList.add(new MyFile(file));
            }
            // ignore the rest
            else
                Log.d("getFileNames()","FILE_DETECTED: "+name);
        }
        currentSelectedItems.clear();
    }

    //
    // getFileExtension:
    //       returns the file-extension derived from the filename string
    //
    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

}







