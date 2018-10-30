package com.example.josephsteccato.id3_edit;

import android.os.Parcelable;
import android.renderscript.Float2;

import java.io.File;
import java.text.DecimalFormat;

public class MyFile {

    private File thisFile;
    private String name;
    private int icon;
    private String size;

    public MyFile(File thisFile) {
        this.thisFile = thisFile;
        this.name = thisFile.getName();
        if (thisFile.isDirectory()){
            this.icon = R.drawable.folder;
            this.size = "Directory";
        }

        else{
            this.icon = R.drawable.file;
            this.size = size(thisFile.length());
        }

    }

    public String size(long size){
        String hrSize = "";
        double k = size/1024.0;
        double m  = k/1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }

    public File getThisFile() {
        return thisFile;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public String getSize() { return size; }
}
