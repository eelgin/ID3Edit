package com.example.josephsteccato.id3_edit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class EditTagsActivity extends AppCompatActivity {

    ArrayList<String> fileNames = new ArrayList<String>();
    String workingDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        fileNames = extras.getStringArrayList("FILE_NAMES");
        workingDirectory = extras.getString("DIRECTORY");

        TextView textView = (TextView) findViewById(R.id.textViewDirectory);
        textView.setText("DIRECTORY: " + workingDirectory);

    }
}
