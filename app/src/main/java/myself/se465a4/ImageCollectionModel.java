package myself.se465a4;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ImageCollectionModel extends Observable{

    List imageList;
    ImageModel unaddedImage;

    public ImageCollectionModel(){
        imageList = new ArrayList<>();
        unaddedImage = null;
    }

//    public void loadImage(Uri newImage){
//        imageList.add(new ImageModel(newImage));
//        addPicture();
//    }

    private void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }

    public void addPicture(Uri fileOfNewPic){
        addPicture(new ImageModel(fileOfNewPic));
    }

    public void addPicture(ImageModel modelOfNewPic){
        imageList.add(modelOfNewPic);
        displayImageModel(modelOfNewPic);
    }

    public void displayImageModel(ImageModel Pic){
        unaddedImage = Pic;
        setChangedAndNotify();
        unaddedImage = null;
    }

    public ImageModel getUnaddedImage(){
        return unaddedImage;
    }
}
