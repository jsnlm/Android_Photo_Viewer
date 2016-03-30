package myself.se465a4;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.Observable;
import java.net.URL;

public class ImageModel extends Observable{

    private Uri path;
    private URL pathURL;
    private String fileName;
    private int rating;

    public ImageModel(URL path, String name){
        this(name);
        this.pathURL = path;
    }

    public ImageModel(Uri path, String name){
        this(name);
        this.path = path;
    }

    private ImageModel(String name){
        fileName = name;
        rating = 0;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getPath(){
        return path;
    }
    public URL getPathURL(){
        return pathURL;
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