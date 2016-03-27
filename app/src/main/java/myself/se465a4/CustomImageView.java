package myself.se465a4;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.Observable;
import java.util.Observer;

public class CustomImageView extends LinearLayout implements Observer{

    ImageModel model;
    RatingBar ratingBar;
    Button ClearRating;
    TextView fileName;

    public CustomImageView(Context c, ImageModel x){
        super(c);
        this.model = x;

        this.setOrientation(LinearLayout.VERTICAL);

        ImageView imgPart = new ImageView(c);
        imgPart.setImageURI(model.getPath());

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

    public void setFilter(int filter, String query) {
        int imgRating = model.getRating();
        String fileName = model.getFileName();
        boolean a = (filter != 0) && (model.getRating() < filter);
        boolean b = (query != null) && (!model.getFileName().contains(query));

        if (
            ((filter != 0) && (model.getRating() < filter))||
            ((query != null) && (!model.getFileName().contains(query) ))
        ){
            this.setVisibility(GONE);
        }
        else{
            this.setVisibility(VISIBLE);
        }
    }
}