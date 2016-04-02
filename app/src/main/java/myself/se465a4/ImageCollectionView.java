package myself.se465a4;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageCollectionView extends AppCompatActivity implements Observer {

    ImageCollectionModel model;
    List<CustomImageView> viewList;
    RatingBar filterView;
    int counter;
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


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            GridLayout temp = new GridLayout(this.getApplicationContext());
//            temp.setColumnCount(2);
//            temp.setColumnOrderPreserved(false);
//            temp.setRowOrderPreserved(false);
//
//            GridLayout.LayoutParams first = new GridLayout.LayoutParams();


            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            inflater.inflate(R.layout.picture_area_horizontal, scrollArea, true);

            GridLayout temp = (GridLayout)scrollArea.findViewById(R.id.picture_area);
            int aferht = temp.getColumnCount();
            pictureArea = temp;
        }
        else {
            LinearLayout temp = new LinearLayout(this.getApplicationContext());
            temp.setOrientation(LinearLayout.VERTICAL);
            //        android:id="@+id/picture_area"
            pictureArea = temp;



            pictureArea.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            ScrollView scrollingArea = (ScrollView) findViewById(R.id.scrollView);
            scrollingArea.addView(pictureArea);
        }
        counter = 0;
//        /////////////////////////////////////////////////////////////////////////////////////
//        String mSelectionClause = null;
//        String[] mSelectionArgs = {""};
//        mSelectionArgs[0] = "";
//
//        String[] mProjection = { "file_name", "stringified_uri" };
//
//        Cursor mCursor = getContentResolver().query( UserDictionary.Words.CONTENT_URI, mProjection, null, null, null);
//        try {
//            while (mCursor.moveToNext()) {
//                Log.d("mCursor.getString(0)", mCursor.getString(0));
//                Log.d("mCursor.getString(1)", mCursor.getString(1));
//            }
//        } finally {
//            mCursor.close();
//        }
//        mCursor.close();
        invalidateOptionsMenu();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("onRestart", "");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("onStart", "");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("onResume", "");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("onPause", "");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("onStop", "");
//        Uri mNewUri;
//        for (ImageModel imgModel : model.getImageList()) {
//            ContentValues mNewValues = new ContentValues();
//            mNewValues.put("stringified_uri", imgModel.getPath().toString());
//            mNewValues.put("file_name", imgModel.getFileName());
//
//            mNewUri = getContentResolver().insert(
//                    UserDictionary.Words.CONTENT_URI,
//                    mNewValues
//            );
//        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("onDestroy", "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("onSaveInstanceState", "");
        model.saveToInstanceState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("onRestoreInstanceState", "");
        super.onRestoreInstanceState(savedInstanceState);
        model.restoreFromInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("onCreateOptionsMenu", "");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.rating_filter);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        filterView = (RatingBar) MenuItemCompat.getActionView(filterItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                query = "http://image.shutterstock.com/display_pic_with_logo/160669/160669,1276177714,1/stock-photo-black-man-and-map-of-africa-on-the-background-54928063.jpg";

                String[] idk = query .split("/");
                String fileName = idk[idk.length-1];

                try{
//                    URL someURL = new URL(query);
                    model.addPicture(query, fileName);
                }
                catch(Exception e){
                    Log.e("URL instantiating error", e.getMessage());
                    e.printStackTrace();
                }


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
//            System.out.println("new image was added");
            CustomImageView newImageView = new CustomImageView(this.getApplicationContext(), newImageModel);
            newImageModel.addObserver(newImageView);
            newImageModel.notifyObservers();

            //newImageView.addRatingListener(this::onRate);

            // LinearLayout pictureArea = (LinearLayout) findViewById(R.id.picture_area);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                int argo = pictureArea.getMeasuredWidth();
//                pictureArea.addView(newImageView, pictureArea.getWidth()/2, 650);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0f);
                layoutParams.setGravity(Gravity.CENTER);
                newImageView.setLayoutParams(layoutParams);
//                pictureArea.setMinimumHeight(650);

                pictureArea.addView(newImageView);
            }
            else{
                pictureArea.addView(newImageView);
            }

            viewList.add(newImageView);
        }

        if (filterView != null){
            filterView.setRating((float)model.getFilter());
        }
        for(CustomImageView img : viewList){
//            img.changeLayout(model.getLayout());
            img.setFilter(model.getFilter());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                Log.d("tag, idk", "action_clear was clicked");
                viewList.clear();
                model.clearImagesModels();
//                LinearLayout pictureArea = (LinearLayout) findViewById(R.id.picture_area);
                pictureArea.removeAllViews();
                return true;

            case R.id.action_load:
////                http://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
//                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                getIntent.setType("image/*");
//
//                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                pickIntent.setType("image/*");
//
//                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
//                startActivityForResult(chooserIntent, 1);

                load10Picture();

                return true;
            case R.id.clear_filter:
                Log.d("tag, idk", "clear_filter was clicked");
                model.setFilter(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
////        http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app
//        switch(requestCode) {
//            case 1:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();
//
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String filePath = cursor.getString(columnIndex);
//                    cursor.close();
//
////                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//
//                    File tempFile = new File(filePath);
//                    String fileName = tempFile.getName();
//                    model.addPicture(selectedImage, fileName);
//
//                }
//        }
//    }

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
