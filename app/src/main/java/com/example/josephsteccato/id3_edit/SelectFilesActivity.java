package com.example.josephsteccato.id3_edit;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import hendrawd.storageutil.library.StorageUtil;

import static com.example.josephsteccato.id3_edit.R.id.mybutton;

public class SelectFilesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyFileAdapter adapter;
    String currentPath;

    int editMode;

    Set<String> extensions = new HashSet<String>();

    ArrayList<MySongInfo> myFileList;
    private ArrayList<MySongInfo> currentSelectedItems;

    private boolean outerDirectory = false;

    //
    // onCreate
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_files);

        Intent intent = getIntent();
        editMode = intent.getIntExtra("EDIT_MODE",0);

        extensions.add(".mp3");
        extensions.add(".m4a");
        extensions.add(".wav");
        extensions.add(".ogg");
        extensions.add(".flac");


        myFileList = new ArrayList<>();
        currentSelectedItems = new ArrayList<>();

        // create recyclerView for viewing files in CardViews
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set current path equal to default "Music" folder and open
        String[] externalPaths = StorageUtil.getStorageDirectories(this);

        currentPath = externalPaths[0];
        initializeFiles();
    }

    //
    // onCreateOptionsMenu
    //      "edit-tags" buttons in action-bar
    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //
    /// onOptionsItemSelected
    //      handle action-bar button events
    //
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            openTagEditor();
        }
        if (id == R.id.buttonAddAll) {
            currentSelectedItems.addAll(myFileList);
            for(int i=0; i<myFileList.size(); ++i){
                if(adapter.checkedItems.get(i))
                    adapter.checkedItems.put(i,false);
                else
                    adapter.checkedItems.put(i, true);
            }
            adapter.notifyDataSetChanged();

        }
        return super.onOptionsItemSelected(item);
    }


    //
    // onBackPressed()
    //      override back-button function
    //
    @Override
    public void onBackPressed(){
        myFileList.clear();
        if(!outerDirectory){
            outerDirectory = previousList();
        }else{
            super.onBackPressed();
            outerDirectory = false;
        }

    }

    //
    // openTagEditor
    //      Bundle filenames and working directory to pass to next activity
    //
    public void openTagEditor(){

        ArrayList<String> fileNames = new ArrayList<>();
        for (MySongInfo file:currentSelectedItems)
            fileNames.add(file.getName());

        Intent intent;
        if(editMode==0)
            intent = new Intent(this, EditTagsActivity.class);
        else
            intent = new Intent(this, InferTagsActivity.class);

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
            public void onFileCheck(MySongInfo file) {
                currentSelectedItems.add(file);
                Log.d("CONSOLE:","File checked: "+ file.getName());
            }

            @Override
            public void onFileUncheck(MySongInfo file) {
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
        outerDirectory = false;
        currentPath = currentPath.concat(path);
        myFileList.clear();
        getFileNames();
        adapter.notifyDataSetChanged();
        adapter.checkedItems.clear();
    }

    //
    // previousList:
    //       navigate to the parent directory
    //
    protected boolean previousList(){
        // check if outermost directory
        if(!currentPath.equals(Environment.getExternalStorageDirectory() + File.separator)) {
            String temp = currentPath.substring(0,currentPath.length()-2);
            currentPath = temp.substring(0,temp.lastIndexOf("/")).concat(File.separator);

            myFileList.clear();
            getFileNames();
            adapter.notifyDataSetChanged();
            adapter.checkedItems.clear();
            return false;
        }else{
            Toast.makeText(this, "Directory Limit Reached\nClick back to return", Toast.LENGTH_SHORT).show();
            return true;
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
                myFileList.add(new MySongInfo(file));
                Log.d("getFileNames()","DIRECTORY: "+name);
            }
            // add audio files
            else if (extensions.contains(getFileExtension(name))){
                Log.d("getFileNames()","AUDIO_FILE_DETECTED: "+name);
                myFileList.add(new MySongInfo(file));
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









