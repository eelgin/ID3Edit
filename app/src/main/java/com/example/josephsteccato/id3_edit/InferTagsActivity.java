package com.example.josephsteccato.id3_edit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InferTagsActivity extends AppCompatActivity {

    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> fileNamesNoExtension = new ArrayList<>();
    ArrayList<MySongInfo> mySongs = new ArrayList<>();
    ArrayList<String> shortFileNames = new ArrayList<>();
    String workingDirectory;
    String fileList = new String();


    Set<String> formatInputOptions = new HashSet<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infer_tags);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        fileNames = extras.getStringArrayList("FILE_NAMES");
        workingDirectory = extras.getString("DIRECTORY");

        formatInputOptions.add("[CDNUMBER]");
        formatInputOptions.add("[TRACKNAME]");
        formatInputOptions.add("[TRACKNUMBER]");
        formatInputOptions.add("[ARTIST]");
        formatInputOptions.add("[ALBUM]");
        formatInputOptions.add("[NULL]");


        // prepare for no. of files to edit
        if (fileNames == null || fileNames.isEmpty()) {
            Toast.makeText(this, "fileNames is empty or null", Toast.LENGTH_SHORT).show();
        } else {
            // show all filenames w/ paths
            // get string-formatting input
            // preview?
            // apply
            listFiles();
        }
    }

    public void listFiles() {

        String directoryArray[] = workingDirectory.split("/");
        String thisDirectory = File.separator + directoryArray[directoryArray.length - 2]
                .concat(File.separator + directoryArray[directoryArray.length - 1]
                        .concat(File.separator));

        for (String fileName : fileNames) {
            File thisFile = new File(workingDirectory + fileName);
            MySongInfo mySong = new MySongInfo(thisFile);
            mySongs.add(mySong);
            String songName = fileName.substring(0, fileName.lastIndexOf("."));
            String temp = thisDirectory.concat(songName);
            fileList += songName + "\n";
            fileNamesNoExtension.add(songName);
        }
        TextView textView = findViewById(R.id.textView4);
        textView.setText(fileList);
    }

    public void inferTags(View view) {
        // split strings by "-", " ", and "_"
        String splitKey = "[-\\s_]";
        EditText editTextInput = findViewById(R.id.editTextInput);
        String rawInput = editTextInput.getText().toString();

        // a string array containing the identifiers (formatInputOptions)
        String formatSplit[] = rawInput.split("/");
        int formatSplitSize = formatSplit.length;

        Toast.makeText(this, formatSplit[0] + " " + formatSplit[1], Toast.LENGTH_SHORT).show();


        // for each filename, split by folder
        for (String thisFile : fileNamesNoExtension) {
            String nameSplit[] = thisFile.split("/");

            int nameSplitSize = nameSplit.length;
            int sizeDiff = nameSplitSize - formatSplitSize;

            // creates a string which matches the length of the input by paths/folders
            // parse each path folder or "split" section, starting with the furthest
            for (int i = 0; i < formatSplitSize; ++i) {
                String thisSplit = (nameSplit[i + sizeDiff]);
                Toast.makeText(this, thisSplit, Toast.LENGTH_SHORT).show();

            }


        }
    }

    public void getTagsFromKeys(String thisSplit){

    }


    public void input(View view){
        EditText editText = findViewById(R.id.editTextInput);
        String currentText = editText.getText().toString();
        String newText = new String();
        switch(view.getId()){
            case R.id.buttonTrackName: {
                newText = currentText.concat("[TRACKNAME]");
                break;
            }
            case R.id.buttonTrackNumber: {
                newText = currentText.concat("[TRACKNUMBER]");
                break;
            }
            case R.id.buttonCDNumber: {
                newText = currentText.concat("[CDNUMBER]");
                break;
            }
        }
        editText.setText(newText);
        editText.setSelection(editText.getText().length());
    }


}
