package myself.se465a4;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageCollectionView extends AppCompatActivity implements Observer {

    ImageCollectionModel model;
    List<CustomImageView> viewList;
    RatingBar filterView;
    ViewGroup pictureArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "");

        model = new ImageCollectionModel();
        model.addObserver(this);
        viewList = new ArrayList<CustomImageView>();

        setContentView(R.layout.activity_main_view2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        ScrollView scrollArea = (ScrollView) findViewById(R.id.scrollView);
        setSupportActionBar(myToolbar);


        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            inflater.inflate(R.layout.picture_area_horizontal, scrollArea, true);
        }
        else {
            inflater.inflate(R.layout.picture_area_vertical, scrollArea, true);
        }
        pictureArea = (ViewGroup)scrollArea.findViewById(R.id.picture_area);
        invalidateOptionsMenu();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        model.saveToInstanceState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        model.restoreFromInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.rating_filter);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        filterView = (RatingBar) MenuItemCompat.getActionView(filterItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String[] idk = query .split("/");
                String fileName = idk[idk.length-1];
                model.addPicture(query, fileName);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        filterView.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.setFilter((int) rating);
            }
        });
        filterView = (RatingBar) MenuItemCompat.getActionView(filterItem);
        filterView.setStepSize(1.0f);
        filterView.setNumStars(5);

        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        ImageModel newImageModel =  model.getUnaddedImage();
        if (newImageModel != null){
            CustomImageView newImageView = new CustomImageView(this.getApplicationContext(), newImageModel);
            newImageModel.addObserver(newImageView);
            newImageModel.notifyObservers();

            newImageView.setCustomEventListener(new OnCustomEventListener() {
                @Override
                public void onEvent() {
                    model.setChangedAndNotify();
                }
            });

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0f);
                layoutParams.setGravity(Gravity.CENTER);
                newImageView.setLayoutParams(layoutParams);
            }

            viewList.add(newImageView);
        }

        if (filterView != null){
            filterView.setRating((float)model.getFilter());
        }
        pictureArea.removeAllViews();
        for(CustomImageView img : viewList){
            if (img.isVisible(model.getFilter())){
                pictureArea.addView(img);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                viewList.clear();
                model.clearImagesModels();
                pictureArea.removeAllViews();
                return true;

            case R.id.action_load:
                load10Picture();
                return true;
            case R.id.clear_filter:
                model.setFilter(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void load10Picture(){
        Uri path1 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture1);
        Uri path2 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture2);
        Uri path3 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture3);
        Uri path4 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture4);
        Uri path5 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture5);
        Uri path6 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture6);
        Uri path7 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture7);
        Uri path8 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture8);
        Uri path9 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture9);
        Uri path10 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture10);
        Uri path11 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture11);
        Uri path12 = Uri.parse("android.resource://myself.se465a4/" + R.drawable.picture12);

        model.addPicture(path1, "picture1.png");
        model.addPicture(path2, "picture2.gif");
        model.addPicture(path3, "picture3.gif");
        model.addPicture(path4, "picture4.jpg");
        model.addPicture(path5, "picture5.jpg");
        model.addPicture(path6, "picture6.jpg");
        model.addPicture(path7, "picture7.jpg");
        model.addPicture(path8, "picture8.png");
        model.addPicture(path9, "picture9.png");
        model.addPicture(path10, "picture10.jpg");
        model.addPicture(path11, "picture11.jpg");
        model.addPicture(path12, "picture12.jpg");
    }
}
