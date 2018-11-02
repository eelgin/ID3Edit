package com.example.josephsteccato.id3_edit;

import android.media.MediaMetadataRetriever;

import java.io.File;

public class MySongInfo extends MyFile{
    private String songTitle;
    private String songArtist;
    private String songAlbum;
    private String songAlbumArtist;
    private String songTrackNumber;
    private String songGenre;
    private String songYear;
    private String songDiscNumber;

    public MySongInfo(File thisFile) {
        super(thisFile);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(thisFile.getAbsolutePath());
        songTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        songArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        songAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        songAlbumArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        songTrackNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
        songGenre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        songYear = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);

        songDiscNumber = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER);
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

    public String getSongTrackNumber() {
        return songTrackNumber;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public String getSongYear() {
        return songYear;
    }

    public String getSongDiscNumber() {
        return songDiscNumber;
    }




}
