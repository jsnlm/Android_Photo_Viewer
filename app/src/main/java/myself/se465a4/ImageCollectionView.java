package myself.se465a4;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageCollectionView extends AppCompatActivity implements Observer {

    ImageCollectionModel model;
    List viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ImageCollectionModel();
        model.addObserver(this);
        viewList = new ArrayList<>();

        setContentView(R.layout.activity_main_view2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                Log.d("item collapsed", searchView.getQuery().toString());
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                Log.d("item expanded", searchView.getQuery().toString());
                return true;  // Return true to expand action view
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem filterItem = menu.findItem(R.id.rating_filter);
        RatingBar filterView = (RatingBar) MenuItemCompat.getActionView(filterItem);
        filterView.setStepSize(1.0f);
        filterView.setNumStars(5);

        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        ImageModel newImageModel =  model.getUnaddedImage();
        if (newImageModel != null){
            System.out.println("new image was added");
            CustomImageView newImageView = new CustomImageView(this.getApplicationContext(), newImageModel);
            newImageModel.addObserver(newImageView);
            newImageModel.notifyObservers();

            //newImageView.addRatingListener(this::onRate);

            LinearLayout pictureArea = (LinearLayout) findViewById(R.id.picture_area);
            pictureArea.addView(newImageView);
            viewList.add(newImageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                Log.d("tag, idk", "action_clear was clicked");
                return true;

            case R.id.action_load:
//                http://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, 1);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    model.addPicture(selectedImage);
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String filePath = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                }
        }
    }
}
