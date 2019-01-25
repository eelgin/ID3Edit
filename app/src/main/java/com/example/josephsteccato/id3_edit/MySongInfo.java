package com.example.josephsteccato.id3_edit;

import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;

import java.io.File;

public class MySongInfo extends MyFile{
    private String songTitle;
    private String songArtist;
    private String songAlbum;
    private String songAlbumArtist;
    private String songTrackNumber;
    private String songTotalTrackNumber;
    private String songGenre;
    private String songYear;

    public MySongInfo(File thisFile) {
        super(thisFile);

        if(thisFile.isFile()){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(thisFile.getAbsolutePath());

            songTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            songArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            songAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            songAlbumArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            songGenre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            songYear = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);

            // Retriever returns Track# as [TRACK#]/[TOTAL#TRACKS]. For example: "3/12"
            String trackNumberFull = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
            String trackNumberSplit [] = {"",""};
            if(trackNumberFull != null){
                if(trackNumberFull.contains("/"))
                    trackNumberSplit = trackNumberFull.split("/");
                else if(trackNumberFull.length() > 0){
                    trackNumberSplit = new String[]{trackNumberFull, ""};
                }
            }
            else
                trackNumberSplit = new String[]{"", ""};

            songTrackNumber = (trackNumberSplit[0]);
            songTotalTrackNumber = (trackNumberSplit[1]);
        }

    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public String getSongAlbumArtist() {
        return songAlbumArtist;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public String getSongYear() {
        return songYear;
    }

    public String getSongTrackNumber() {
        return songTrackNumber;
    }

    public String getSongTotalTrackNumber() {
        return songTotalTrackNumber;
    }





}
