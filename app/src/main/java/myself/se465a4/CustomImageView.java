package myself.se465a4;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class CustomImageView extends LinearLayout implements Observer{

    ImageModel model;
    RatingBar ratingBar;
    Button ClearRating;
    TextView fileName;

    public CustomImageView(Context c, ImageModel x){
        super(c);
        this.model = x;

        this.setOrientation(LinearLayout.VERTICAL);

        Random r = new Random();
        this.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

//        Bitmap yourSelectedImage = BitmapFactory.decodeFile(model.getPath().getPath());

        ImageView imgPart = new ImageView(c);
        if (model.getPath() != null){
            imgPart.setImageURI(model.getPath());
        }
        else{
            try{
                new DownloadImageTask(imgPart).execute(model.getPathURL().toString());
            }
            catch(Exception e){
                Log.e("URL getting error", e.getStackTrace().toString());
                e.printStackTrace();
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imgPart.setAdjustViewBounds(true);
            imgPart.setMaxHeight(400);
            imgPart.setMinimumHeight(400);
        }

        ratingBar = new RatingBar(c);
        ratingBar.setStepSize(1.0f);
        ratingBar.setNumStars(5);
        ratingBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Drawable stars = ratingBar.getProgressDrawable();
        stars.setTint(Color.BLACK);

        ClearRating = new Button(c);
        ClearRating.setText(R.string.clear_rating);

        fileName = new TextView(c);
        fileName.setText(model.getFileName());
        fileName.setTextColor(Color.BLACK);

        this.addView(imgPart);
        this.addView(fileName);
        this.addView(ratingBar);
        this.addView(ClearRating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.setRating((int) rating);
            }
        });

        ClearRating.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                model.setRating(0);
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        ratingBar.setRating((float)model.getRating());
    }

    public void setFilter(int filter) {
        if ((filter != 0) && (model.getRating() < filter)) {
            this.setVisibility(GONE);
        }
        else {
            this.setVisibility(VISIBLE);
        }
    }

    // Plagiarized from http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}