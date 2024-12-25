package com.si_charginganimation.nilesh_charginganimation.model;

import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class SongModel {

    String songPath;
    String songUri;
    String songTitle;
    String songDuration;
    String songAlbum;
    String songArtists;
    String songAlbumArtists;
    String songLm;
    String folderName;
    String thumb;
    long size;


    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongUri(Uri songUri) {
        this.songUri = songUri.toString();
    }

    public Uri getSongUri() {
        return Uri.parse(songUri);
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongArtists(String songArtists) {
        this.songArtists = songArtists;
    }

    public String getSongArtists() {
        return songArtists;
    }

    public void setSongAlbumArtists(String songAlbumArtists) {
        this.songAlbumArtists = songAlbumArtists;
    }

    public String getSongAlbumArtists() {
        return songAlbumArtists;
    }

    public void setSongLm(String songLm) {
        this.songLm = songLm;
    }

    public String getSongLm() {
        return songLm;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public static Comparator<SongModel> StuNameComparator = new Comparator<SongModel>() {


        @Override
        public int compare(SongModel songModel, SongModel t1) {
            String StudentName1 = songModel.getSongTitle().toUpperCase();
            String StudentName2 = t1.getSongTitle().toUpperCase();
            return StudentName1.compareTo(StudentName2);


            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    public static Comparator<SongModel> ListSortZtoA = new Comparator<SongModel>() {


        @Override
        public int compare(SongModel songModel, SongModel t1) {
            String StudentName1 = songModel.getSongTitle().toUpperCase();
            String StudentName2 = t1.getSongTitle().toUpperCase();
            return StudentName2.compareTo(StudentName1);


        }
    };
    public static Comparator<SongModel> ListLM = new Comparator<SongModel>() {


        @Override
        public int compare(SongModel songModel, SongModel t1) {

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");


            try {
                return f.parse(songModel.getSongLm()).compareTo(f.parse(t1.getSongLm()));
            } catch (ParseException e) {
                e.printStackTrace();

                throw new IllegalArgumentException(e);
            }


        }
    };
    public static Comparator<SongModel> ListLMD = new Comparator<SongModel>() {


        @Override
        public int compare(SongModel songModel, SongModel t1) {

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");


            try {
                 return f.parse(t1.getSongLm()).compareTo(f.parse(songModel.getSongLm()));
            } catch (ParseException e) {
                e.printStackTrace();

                throw new IllegalArgumentException(e);
            }


        }
    };

    public static Comparator<SongModel> ListSize = new Comparator<SongModel>() {


        @Override
        public int compare(SongModel songModel, SongModel t1) {


             return  Long.valueOf(songModel.getSize()).compareTo(t1.getSize());


        }
    };
    public static Comparator<SongModel> ListShuffle = new Comparator<SongModel>() {







        @Override
        public int compare(SongModel songModel, SongModel t1) {
            String StudentName1 = songModel.getSongAlbum().toUpperCase();
            String StudentName2 = t1.getSongAlbum().toUpperCase();
            return StudentName2.compareTo(StudentName1);

        }
    };
    public static Comparator<SongModel> ListShuffle2 = new Comparator<SongModel>() {
         @Override
        public int compare(SongModel songModel, SongModel t1) {
            String StudentName1 = songModel.getSongAlbum().toUpperCase();
            String StudentName2 = t1.getSongAlbum().toUpperCase();
            return StudentName1.compareTo(StudentName2);

        }
    };

}

