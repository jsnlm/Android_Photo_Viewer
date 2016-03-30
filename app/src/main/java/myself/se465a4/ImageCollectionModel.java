package myself.se465a4;

import android.net.Uri;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ImageCollectionModel extends Observable{

    List<ImageModel> imageList;
    ImageModel unaddedImage;
    int filter;

    public ImageCollectionModel(){
        imageList = new ArrayList<ImageModel>();
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

    public void addPicture(URL fileOfNewPic, String fileName){
        addPicture(new ImageModel(fileOfNewPic, fileName));
    }

    public void addPicture(Uri fileOfNewPic, String fileName){
        addPicture(new ImageModel(fileOfNewPic, fileName));
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

    //////////////////////////////////// Getters and Setters ///////////////////////////////////////

    public ImageModel getUnaddedImage(){
        return unaddedImage;
    }
    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
        setChangedAndNotify();
    }

    public List<ImageModel> getImageList() {
        return imageList;
    }

    public void clearImagesModels(){
        imageList.clear();
    }
}
