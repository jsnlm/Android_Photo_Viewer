package myself.se465a4;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import org.xmlpull.v1.XmlPullParser;

import java.util.Observable;
import java.util.Observer;

public class CustomImageView extends LinearLayout implements Observer{

    ImageModel model;
    RatingBar ratingBar;

    public CustomImageView(Context c, ImageModel x){
        super(c);
        this.model = x;

//        LinearLayout ll = new LinearLayout(c);
//        ll.setId(imagePath.hashCode());
        this.setOrientation(LinearLayout.VERTICAL);

        ImageView imgPart = new ImageView(c);
        imgPart.setImageURI(model.getPath());

//        XmlPullParser parser = getResources().getXml(R.style.testStyle);
//        AttributeSet attributes = Xml.asAttributeSet(parser);

        ratingBar = new RatingBar(c);
        ratingBar.setStepSize(1.0f);
//        ratingBar.setRating(5);
//        ratingBar.setMax(5);
        ratingBar.setNumStars(5);

        ratingBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Drawable stars = ratingBar.getProgressDrawable();
        stars.setTint(Color.BLACK);
//        ratingBar.setBackgroundResource(R.drawable.rating_bar_star_filled);
//        ratingBar.setSecondaryProgress();
//        ratingBar.setProgress(R.drawable.rating_bar_star);


//        ColorStateList stateList = ColorStateList.valueOf(100);
//        ratingBar.setProgressTintList(stateList);
//        ratingBar.setSecondaryProgressTintList(stateList);
//        ratingBar.setIndeterminateTintList(stateList);


//        ViewGroup.LayoutParams aefjiagr = ratingBar.getLayoutParams();
//        aefjiagr.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        this.addView(imgPart);
        this.addView(ratingBar);
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}

