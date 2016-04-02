package myself.se465a4;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.MediaStore;

import java.io.File;
import java.util.Observable;
import java.net.URL;
import android.os.Parcelable;

public class ImageModel extends Observable implements Parcelable{

    private Uri path;
    private String pathURL;
    private String fileName;
    private int rating;

    public ImageModel(String path, String name){
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

    private ImageModel(Parcel in){
        this.path = Uri.parse(in.readString());
        this.pathURL = in.readString();
        this.fileName = in.readString();
        this.rating = in.readInt();
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getPath(){
        return path;
    }
    public String getPathURL(){
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

    @Override
    public int describeContents() {
        return 54321;
    }

    public static final Parcelable.Creator<ImageModel> CREATOR = new Parcelable.Creator<ImageModel>() {

        @Override
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Uri.writeToParcel(dest, path);
        dest.writeString(pathURL);
        dest.writeString(fileName);
        dest.writeInt(rating);
    }
}