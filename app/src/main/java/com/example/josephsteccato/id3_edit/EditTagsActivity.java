package com.example.josephsteccato.id3_edit;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EditTagsActivity extends AppCompatActivity {

    ArrayList<String> fileNames = new ArrayList<String>();
    String workingDirectory;

    EditText editTextArtist;
    EditText editTextAlbum;
    EditText editTextAlbumArtist;
    EditText editTextGenre;
    EditText editTextYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        fileNames = extras.getStringArrayList("FILE_NAMES");
        workingDirectory = extras.getString("DIRECTORY");

        editTextArtist = (EditText)findViewById(R.id.editTextArtist);
        editTextAlbum = (EditText)findViewById(R.id.editTextAlbum);
        editTextAlbumArtist = (EditText)findViewById(R.id.editTextAlbumArtist);
        editTextGenre = (EditText)findViewById(R.id.editTextGenre);
        editTextYear = (EditText)findViewById(R.id.editTextYear);

        // prepare for no. of files to edit
        if(fileNames == null || fileNames.isEmpty()){
            Toast.makeText(this, "fileNames is empty or null", Toast.LENGTH_SHORT).show();
        }
        else if(fileNames.size() == 1){
            // set single file
            setSingleFile();
        }else{
            // set multiple files
            setMultipleFiles();
        }
    }

    //
    // applyChanges
    //      get values from TextEdit fields
    //
    //
    public void applyChanges(View view){
        String newArtist = editTextArtist.getText().toString();
        String newAlbum = editTextAlbum.getText().toString();
        String newAlbumArtist = editTextAlbumArtist.getText().toString();
        String newGenre = editTextGenre.getText().toString();
        String newYear = editTextYear.getText().toString();

        Toast.makeText(this, "NEW-INFO: "+newArtist+newAlbum+newYear+newGenre, Toast.LENGTH_LONG).show();


    }

    //
    // setSingleFile
    //      set Tags for a single song
    //      currently only returns song information to corresponding fields
    //
    public void setSingleFile(){
        File file = new File(workingDirectory+fileNames.get(0));

        TextView textView = (TextView) findViewById(R.id.textViewDirectory);
        textView.setText("EDIT SINGLE FILE:\n" + file.getAbsolutePath());

        MySongInfo mySong = new MySongInfo(file);

        editTextArtist.setText(mySong.getSongArtist());
        editTextAlbum.setText(mySong.getSongAlbum());
        editTextAlbumArtist.setText(mySong.getSongAlbumArtist());
        editTextGenre.setText(mySong.getSongGenre());
        editTextYear.setText(mySong.getSongYear());
    }

    //
    // setMultipleFiles()
    //      set Tags for multiple song
    //      currently only returns song information to corresponding fields
    //
    public void setMultipleFiles(){

        // stores each files value for each field
        // can be used in future updates for having the user select possible values
        ArrayList<String> multiArtist = new ArrayList<>();
        ArrayList<String> multiAlbum = new ArrayList<>();
        ArrayList<String> multiAlbumArtist = new ArrayList<>();
        ArrayList<String> multiGenre = new ArrayList<>();
        ArrayList<String> multiYear = new ArrayList<>();

        ArrayList<MySongInfo> mySongs = new ArrayList<>();

        TextView textView = (TextView) findViewById(R.id.textViewDirectory);
        textView.setText("EDIT MULTIPLE FILES:\n" + workingDirectory);

        // collect songinfo for each file
        for(String thisFileName:fileNames){
            File thisFile = new File(workingDirectory + thisFileName);
            MySongInfo mySong = new MySongInfo(thisFile);

            mySongs.add(mySong);
            multiArtist.add(mySong.getSongArtist());
            multiAlbum.add(mySong.getSongAlbum());
            multiAlbumArtist.add(mySong.getSongAlbumArtist());
            multiGenre.add(mySong.getSongGenre());
            multiYear.add(mySong.getSongYear());

            Log.d("editMultipleFiles","ADDED: "+mySong.getSongTitle());
        }

        // compare values and set EditTexts for each field
        checkList(multiArtist,editTextArtist);
        checkList(multiAlbum,editTextAlbum);
        checkList(multiAlbumArtist,editTextAlbumArtist);
        checkList(multiGenre,editTextGenre);
        checkList(multiYear,editTextYear);
    }

    //
    // checkList
    //      Check if all strings in a list match or are null,
    //      otherwise set editText value to placeholder value.
    //
    public void checkList(ArrayList<String> fieldValueList, EditText thisEditText){
        String fieldValue = fieldValueList.get(0);
        for(String thisString:fieldValueList){
            if(fieldValue == null ||!thisString.equals(fieldValue)){
                fieldValue = "";
                break;
            }
        }
        thisEditText.setText(fieldValue);
    }
}
