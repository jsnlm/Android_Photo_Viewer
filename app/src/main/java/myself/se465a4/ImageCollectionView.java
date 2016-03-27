package myself.se465a4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.UserDictionary;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageCollectionView extends AppCompatActivity implements Observer {

    ImageCollectionModel model;
    List<CustomImageView> viewList;
    RatingBar filterView;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ImageCollectionModel();
        model.addObserver(this);
        viewList = new ArrayList<CustomImageView>();

        setContentView(R.layout.activity_main_view2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        counter = 0;
        /////////////////////////////////////////////////////////////////////////////////////
        String mSelectionClause = null;
        String[] mSelectionArgs = {""};
        mSelectionArgs[0] = "";

        String[] mProjection = { "file_name", "stringified_uri" };

        Cursor mCursor = getContentResolver().query( UserDictionary.Words.CONTENT_URI, mProjection, null, null, null);
        try {
            while (mCursor.moveToNext()) {
                Log.d("mCursor.getString(0)", mCursor.getString(0));
                Log.d("mCursor.getString(1)", mCursor.getString(1));
            }
        } finally {
            mCursor.close();
        }
        mCursor.close();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("onRestart", "onRestart");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("onStart", "onStart" + counter++);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("onResume", "onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("onPause", "onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("onStop", "onStop");

        Uri mNewUri;

        for (ImageModel imgModel : model.getImageList()) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put("stringified_uri", imgModel.getPath().toString());
            mNewValues.put("file_name", imgModel.getFileName());

            mNewUri = getContentResolver().insert(
                    UserDictionary.Words.CONTENT_URI,
                    mNewValues
            );
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy", "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.rating_filter);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        filterView = (RatingBar) MenuItemCompat.getActionView(filterItem);

        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                Log.d("item collapsed", searchView.getQuery().toString());
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                Log.d("item expanded", searchView.getQuery().toString());
                return true;
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")){
                    model.setSearchFilter(null);
                }
                else{
                    model.setSearchFilter(newText);
                }
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
            System.out.println("new image was added");
            CustomImageView newImageView = new CustomImageView(this.getApplicationContext(), newImageModel);
            newImageModel.addObserver(newImageView);
            newImageModel.notifyObservers();

            //newImageView.addRatingListener(this::onRate);

            LinearLayout pictureArea = (LinearLayout) findViewById(R.id.picture_area);
            pictureArea.addView(newImageView);
            viewList.add(newImageView);
        }

        filterView.setRating((float)model.getFilter());
        for(CustomImageView img : viewList){
//            img.changeLayout(model.getLayout());
            img.setFilter(model.getFilter(), model.getSearchFilter());
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
            case R.id.clear_filter:
                Log.d("tag, idk", "clear_filter was clicked");
                model.setFilter(0);
                model.setSearchFilter(null);
                return true;
            default:
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

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

//                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                    File tempFile = new File(filePath);
                    String fileName = tempFile.getName();
                    model.addPicture(selectedImage, fileName);

                }
        }
    }
}
