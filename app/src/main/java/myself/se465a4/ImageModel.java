package myself.se465a4;

import android.net.Uri;
import java.util.Observable;

public class ImageModel extends Observable{

    private Uri path;
    private int rating;
    private CustomImageView view;

    public ImageModel(Uri path){
        this.path = path;
        rating = 0;
    }

    public Uri getPath(){
        return path;
    }
}