package com.example.josephsteccato.id3_edit;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SelectFilesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyFileAdapter adapter;
    String currentPath;

    ArrayList<MyFile> myFileList;
    private ArrayList<MyFile> currentSelectedItems;

    //
    // onCreate
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_files);

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

    public void openTagEditor(View view){
        Intent intent = new Intent(this, EditTagsActivity.class);
        intent.putExtra("filesToTag", currentSelectedItems);
        startActivity(intent);

    }

    //
    // initializeFiles:
    //      pass a directory path to a string and view the contents in the RecyclerView
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
        //Log.d("CONSOLE","currentPath = "+currentPath);

        File directory = new File(currentPath);
        File[] file_array = directory.listFiles();

        for (File file:file_array){
            myFileList.add(new MyFile(file));
            //Log.d("CONSOLE",file.getName()+": "+file.exists());
        }
        currentSelectedItems.clear();
    }
}







