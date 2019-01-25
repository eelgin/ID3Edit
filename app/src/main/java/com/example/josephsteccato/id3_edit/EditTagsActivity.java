package com.example.josephsteccato.id3_edit;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    ArrayList<String> multiArtist = new ArrayList<>();
    ArrayList<String> multiAlbum = new ArrayList<>();
    ArrayList<String> multiAlbumArtist = new ArrayList<>();
    ArrayList<String> multiGenre = new ArrayList<>();
    ArrayList<String> multiYear = new ArrayList<>();
    ArrayList<String> multiTitle = new ArrayList<>();
    ArrayList<String> multiTrackNumber = new ArrayList<>();
    ArrayList<String> multiTotalTrackNumber = new ArrayList<>();

    ArrayList<MySongInfo> mySongs = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        fileNames = extras.getStringArrayList("FILE_NAMES");
        workingDirectory = extras.getString("DIRECTORY");
        Toast.makeText(this, "Directory: "+workingDirectory, Toast.LENGTH_SHORT).show();

        editTextArtist = (EditText)findViewById(R.id.editTextArtist);
        editTextAlbum = (EditText)findViewById(R.id.editTextAlbum);
        editTextAlbumArtist = (EditText)findViewById(R.id.editTextAlbumArtist);
        editTextGenre = (EditText)findViewById(R.id.editTextGenre);
        editTextYear = (EditText)findViewById(R.id.editTextYear);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextNumberTop = (EditText)findViewById(R.id.editTextNumberTop);
        editTextNumberBottom = (EditText)findViewById(R.id.editTextNumberBottom);

        // prepare for no. of files to edit
        if(fileNames == null || fileNames.isEmpty()){
            Toast.makeText(this, "fileNames is empty or null", Toast.LENGTH_SHORT).show();
        }
        else if(fileNames.size() == 1){
            // set single file
            Button buttonViewSongs = (Button)findViewById(R.id.buttonViewSongs);
            buttonViewSongs.setVisibility(View.INVISIBLE);

            setFiles();
        }
        else{
            // set multiple files
            editTextTitle.setFocusable(false);
            editTextTitle.setClickable(false);
            editTextTitle.setEnabled(false);
            editTextTitle.setHint("Title [MULTIPLE TRACKS SELECTED]");

            editTextNumberTop.setFocusable(false);
            editTextNumberTop.setClickable(false);
            editTextNumberTop.setEnabled(false);

            setFiles();
        }
    }

    //
    // showAllSongs
    //      Toast containing all track names and numbers
    //
    public void showAllSongs(View view){
        //Toast.makeText(this, songs, Toast.LENGTH_LONG).show();
        ArrayList<String> trackAndNumber = new ArrayList<>();
        for(int i=0; i<multiTitle.size(); ++i){
            trackAndNumber.add(multiTrackNumber.get(i)+": \t"+multiTitle.get(i));
        }
        String[] titles = trackAndNumber.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tracks:")
                .setItems(titles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();

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

            Toast.makeText(this, "Name: "+thisFile, Toast.LENGTH_SHORT).show();
            try{
                AudioFile f = AudioFileIO.read(new File(workingDirectory+thisFile));
                org.jaudiotagger.tag.Tag tag = f.getTag();
                if(!newArtist.equals("")){tag.setField(FieldKey.ARTIST,newArtist);}

                if(!newAlbum.equals("")){tag.setField(FieldKey.ALBUM,newAlbum);}
                if(!newAlbumArtist.equals("")){tag.setField(FieldKey.ALBUM_ARTIST,newAlbumArtist);}
                if(!newGenre.equals("")){tag.setField(FieldKey.GENRE,newGenre);}
                if(!newYear.equals("")){tag.setField(FieldKey.YEAR,newYear);}
                if(!newTotalNumber.equals("")){tag.setField(FieldKey.TRACK_TOTAL,newTotalNumber);}

                if (fileNames.size() == 1){
                    if(!newTitle.equals("")){tag.setField(FieldKey.TITLE,newTitle);}
                    if(!newNumber.equals("")){tag.setField(FieldKey.TRACK,newNumber);}
                }
                AudioFileIO.write(f);
            }
            catch (org.jaudiotagger.audio.exceptions.CannotReadException e) {
                System.out.println("jAudioTagger CannotReadException|File: "+thisFile+"|Message: "+e.getMessage());
                Toast.makeText(this, "jAudioTagger CannotReadException\n\n"+ thisFile+"\n"+e.getMessage()
                        , Toast.LENGTH_LONG).show();
            }
            catch (org.jaudiotagger.audio.exceptions.NoWritePermissionsException e) {
                System.out.println("jAudioTagger NoWritePermissions|File: "+thisFile+"|Message: "+e.getMessage());
                Toast.makeText(this, "jAudioTagger NoWritePermissions\n\n"+ thisFile+"\n"+e.getMessage()
                        , Toast.LENGTH_LONG).show();
            }

        }
        //Toast.makeText(this, "Tags Updated!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //
    // setSingleFile
    //      set Tags for a single song
    //      currently only returns song information to corresponding fields
    //
//    public void setSingleFile(){
//        File file = new File(workingDirectory+fileNames.get(0));
//
//        TextView textView = (TextView) findViewById(R.id.textViewDirectory);
//        textView.setText("File: "+file.getAbsolutePath());
//        TextView textViewMode = (TextView) findViewById(R.id.textViewMode);
//        textViewMode.setText("Single File");
//
//        MySongInfo mySong = new MySongInfo(file);
//
//        editTextArtist.setText(mySong.getSongArtist());
//        editTextAlbum.setText(mySong.getSongAlbum());
//        editTextAlbumArtist.setText(mySong.getSongAlbumArtist());
//        editTextGenre.setText(mySong.getSongGenre());
//        editTextYear.setText(mySong.getSongYear());
//        editTextTitle.setText(mySong.getSongTitle());
//        editTextNumberTop.setText(mySong.getSongTrackNumber());
//        editTextNumberBottom.setText(mySong.getSongTotalTrackNumber());
//    }

    //
    // set]Files()
    //      set Tags for single or multiple song
    //      currently only returns song information to corresponding fields
    //
    public void setFiles(){

        TextView textView = (TextView) findViewById(R.id.textViewDirectory);
        textView.setText("Location: \n"+workingDirectory);
        TextView textViewMode = (TextView) findViewById(R.id.textViewMode);
        textViewMode.setText("Multiple Files");

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
            multiTrackNumber.add(mySong.getSongTrackNumber());
            multiTotalTrackNumber.add(mySong.getSongTotalTrackNumber());
            Log.d("editMultipleFiles","ADDED: "+mySong.getSongTitle());
        }

        // compare values and set EditTexts for each field
        checkList(multiArtist,editTextArtist);
        checkList(multiAlbum,editTextAlbum);
        checkList(multiAlbumArtist,editTextAlbumArtist);
        checkList(multiGenre,editTextGenre);
        checkList(multiYear,editTextYear);
        checkList(multiTotalTrackNumber,editTextNumberBottom);
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
