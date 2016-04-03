package myself.se465a4;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class CloseupImageLayout extends AppCompatActivity {

    ImageModel toBeDeisplaed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closeup_image_layout);

        ImageView imageDisplay = (ImageView) findViewById(R.id.image_display);

        Intent intent = getIntent();
        // Ideally a parcel of ImageModel would be passed but I couldn't get it to work. This is the bad workaround
        Uri theURI = intent.getParcelableExtra("Image_Link_Uri");
        String pathURL = intent.getStringExtra("Image_Link_URL");

        if (theURI != null){
            imageDisplay.setImageURI(theURI);
        }
        else{
            new DownloadImageTask(imageDisplay).execute(pathURL);
        }

        imageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
