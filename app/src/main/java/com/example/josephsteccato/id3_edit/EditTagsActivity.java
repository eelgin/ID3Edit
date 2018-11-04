package com.example.josephsteccato.id3_edit;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
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
    EditText editTextTitle;
    EditText editTextNumberTop;
    EditText editTextNumberBottom;

    String songs = "Files Selected:";


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
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextNumberTop = (EditText)findViewById(R.id.editTextNumber);
        editTextNumberBottom = (EditText)findViewById(R.id.editTextNumberBottom);


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
    // showAllSongs
    //      Toast containing all track names and numbers
    //
    public void showAllSongs(View view){
        Toast.makeText(this, songs, Toast.LENGTH_LONG).show();
    }

    //
    // applyChanges
    //      get values from TextEdit fields and write changes to metadata
    //
    //
    public void applyChanges(View view) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException, CannotWriteException {
        // get field values
        String newArtist = editTextArtist.getText().toString();
        String newAlbum = editTextAlbum.getText().toString();
        String newAlbumArtist = editTextAlbumArtist.getText().toString();
        String newGenre = editTextGenre.getText().toString();
        String newYear = editTextYear.getText().toString();
        String newTitle = editTextTitle.getText().toString();
        String newNumber = editTextNumberTop.getText().toString();
        String newTotalNumber = editTextNumberBottom.getText().toString();

        // apply changes for each file
        for(String thisFile:fileNames){
            AudioFile f = AudioFileIO.read(new File(workingDirectory+thisFile));
            org.jaudiotagger.tag.Tag tag = f.getTag();

            tag.setField(FieldKey.ARTIST,newArtist);
            tag.setField(FieldKey.ALBUM,newAlbum);
            tag.setField(FieldKey.ALBUM_ARTIST,newAlbumArtist);
            tag.setField(FieldKey.GENRE,newGenre);
            tag.setField(FieldKey.YEAR,newYear);
            tag.setField(FieldKey.TRACK_TOTAL,newTotalNumber);

            if (fileNames.size() == 1){
                tag.setField(FieldKey.TITLE,newTitle);
                tag.setField(FieldKey.TRACK,newNumber);
            }
            f.commit();
        }
        Toast.makeText(this, "Tags Updated!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        editTextTitle.setText(mySong.getSongTitle());

        String trackNumberFull[] = mySong.getSongTrackNumber().split("/");
        editTextNumberTop.setText(trackNumberFull[0]);
        editTextNumberBottom.setText(trackNumberFull[1]);
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
        ArrayList<String> multiTitle = new ArrayList<>();
        ArrayList<String> multiNumber = new ArrayList<>();

        ArrayList<MySongInfo> mySongs = new ArrayList<>();

        TextView textView = (TextView) findViewById(R.id.textViewDirectory);
        textView.setText("EDIT MULTIPLE FILES:\n" + workingDirectory);

        // collect song-info for each file
        for(String thisFileName:fileNames){
            File thisFile = new File(workingDirectory + thisFileName);
            MySongInfo mySong = new MySongInfo(thisFile);

            mySongs.add(mySong);
            multiArtist.add(mySong.getSongArtist());
            multiAlbum.add(mySong.getSongAlbum());
            multiAlbumArtist.add(mySong.getSongAlbumArtist());
            multiGenre.add(mySong.getSongGenre());
            multiYear.add(mySong.getSongYear());
            multiTitle.add(mySong.getSongTitle());
            multiNumber.add(mySong.getSongTrackNumber());

            String temp = "\n"+mySong.getSongTrackNumber()+": "+mySong.getSongTitle();
            songs+=temp;

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
