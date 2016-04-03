package myself.se465a4;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

interface OnCustomEventListener {
    void onEvent();
}

public class CustomImageView extends LinearLayout implements Observer{

    ImageModel model;
    RatingBar ratingBar;
    Button ClearRating;
    TextView fileName;
    OnCustomEventListener mListener;

    public CustomImageView(Context c, ImageModel x){
        super(c);
        this.model = x;

        this.setOrientation(LinearLayout.VERTICAL);

//        Random r = new Random();
//        this.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

        ImageView imgPart = new ImageView(c);
        if (model.getPath() != null){
            imgPart.setImageURI(model.getPath());
        }
        else{
            try{
                new DownloadImageTask(imgPart).execute(model.getPathURL());
            }
            catch(Exception e){
                Log.e("URL getting error", e.getStackTrace().toString());
                e.printStackTrace();
            }
        }

        imgPart.setAdjustViewBounds(true);
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imgPart.setMaxHeight(400);
            imgPart.setMinimumHeight(400);
//        }

        ratingBar = new RatingBar(c);
        ratingBar.setStepSize(1.0f);
        ratingBar.setNumStars(5);
        ratingBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayerDrawable stars = (LayerDrawable)ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.rgb(80, 80, 80), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.rgb(180, 180, 180), PorterDuff.Mode.SRC_ATOP);
//        stars.setTint(Color.BLACK);

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

        ClearRating.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setRating(0);
            }
        });
        imgPart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CloseupImageLayout.class);
                if (model.getPath() != null){
                    intent.putExtra("Image_Link_Uri", model.getPath());
                } else{
                    intent.putExtra("Image_Link_URL", model.getPathURL());
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                (getContext()).startActivity(intent);
            }
        });
    }
    @Override
    public void update(Observable observable, Object data) {
        ratingBar.setRating((float) model.getRating());
        mListener.onEvent();
    }

    public boolean isVisible(int filter) {
        if ((filter != 0) && (model.getRating() < filter)) {
//            this.setVisibility(GONE);
            return false;
        }
        else {
//            this.setVisibility(VISIBLE);
            return true;
        }
    }

    public void setCustomEventListener(OnCustomEventListener eventListener) {
        mListener = eventListener;
    }

}