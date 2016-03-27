package myself.se465a4;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.Observable;

public class ImageModel extends Observable{

    private Uri path;
    private String fileName;
    private int rating;

    public ImageModel(Uri path, String name){
        this.path = path;
        rating = 0;
        fileName = name;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getPath(){
        return path;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int x){
        rating = x;
        setChangedAndNotify();
    }

    private void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }
}