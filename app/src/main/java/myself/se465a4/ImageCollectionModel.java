package myself.se465a4;

import android.net.Uri;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ImageCollectionModel extends Observable{

    ArrayList<ImageModel> imageList;
    ImageModel unaddedImage;
    int filter;

    public ImageCollectionModel(){
        imageList = new ArrayList<ImageModel>();
        unaddedImage = null;
    }

    private void setChangedAndNotify(){
        setChanged();
        notifyObservers();
    }

    public void addPicture(String fileOfNewPic, String fileName){
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

    public void saveToInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList("ImageModels", imageList);
    }

    public void restoreFromInstanceState(Bundle savedInstanceState){
        imageList = savedInstanceState.getParcelableArrayList("ImageModels");
        for (ImageModel imgMod: imageList) {
            displayImageModel(imgMod);
        }
        setChangedAndNotify();
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
