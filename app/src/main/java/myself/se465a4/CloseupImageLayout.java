package myself.se465a4;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class CloseupImageLayout extends AppCompatActivity {

    ImageModel toBeDeisplaed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closeup_image_layout);



        ImageView imageDisplay = (ImageView) findViewById(R.id.image_display);

        Intent intent = getIntent();
        Uri theURI = intent.getParcelableExtra("Image_Link_Uri");
        String pathURL = intent.getStringExtra("Image_Link_URL");

        if (theURI != null){
            Log.d("CloseupImageLayout was initiated", theURI.toString());
            imageDisplay.setImageURI(theURI);
        }
        else{
            Log.d("CloseupImageLayout was initiated", pathURL);
            new DownloadImageTask(imageDisplay).execute(pathURL);
        }

//        toBeDeisplaed = intent.getExtras().getParcelable("Image_Link");
        int a = 1;
//        String agretg = intent.getStringExtra("someText");
//        Log.d("CloseupImageLayout was initiated", agretg);



    }
}
