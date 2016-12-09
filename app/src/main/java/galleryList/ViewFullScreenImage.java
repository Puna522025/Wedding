package galleryList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import pkapoor.wed.R;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class ViewFullScreenImage extends AppCompatActivity implements View.OnClickListener {

    ImageView imageViewNext, imageViewPrevious, imageFullScreen, imageViewClose;
    ArrayList<Integer> images;
    int position = 0;
    private static int currentPostition = 0;
    String type = "brxxx";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);
        imageViewNext = (ImageView) findViewById(R.id.imageViewNext);
        imageViewPrevious = (ImageView) findViewById(R.id.imageViewPrevious);
        imageFullScreen = (ImageView) findViewById(R.id.imageFullScreen);
        imageViewClose = (ImageView) findViewById(R.id.imageViewClose);


       /* if (getIntent() != null && getIntent().getExtras() != null) {
            position = Integer.parseInt(getIntent().getExtras().get("position").toString());
            type = getIntent().getExtras().get("type").toString();
            currentPostition = position;
        }

        if (type.equalsIgnoreCase("Rxxxx")) {
            images = new ArrayList<>();
            images.add(0, R.drawable.aa);
            images.add(1, R.drawable.ss);
            images.add(2, R.drawable.aa);
        }
        if (type.equalsIgnoreCase("Grxxx")) {
            images = new ArrayList<>();
            images.add(0, R.drawable.aa);
            images.add(1, R.drawable.ss);
        }
        else if (type.equalsIgnoreCase("brxxx")){
            images = new ArrayList<>();
            images.add(0, R.drawable.aa);
            images.add(1, R.drawable.ss);
            images.add(2, R.drawable.aa);
            images.add(3, R.drawable.ss);
            images.add(4, R.drawable.aa);
            images.add(5, R.drawable.ss);
            images.add(6, R.drawable.aa);
            images.add(7, R.drawable.ss);
            images.add(8, R.drawable.aa);
            images.add(9, R.drawable.ss);
            images.add(10, R.drawable.ss);
            images.add(11, R.drawable.aa);
            images.add(12, R.drawable.ss);
        }*/

        imageFullScreen.setImageDrawable(ContextCompat.getDrawable(this, images.get(position)));
        imageViewNext.setOnClickListener(this);
        imageViewPrevious.setOnClickListener(this);
        imageViewClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewNext:
                if (currentPostition + 1 < images.size()) {
                    changeImageNext();
                }
                break;
            case R.id.imageViewPrevious:
                if (currentPostition >= 0) {
                    changeImagePrevious();
                }
                break;
            case R.id.imageViewClose:
                finish();
                break;
        }
    }

    private void changeImagePrevious() {
        currentPostition = currentPostition - 1;
        if (currentPostition >= 0) {
            imageFullScreen.setImageDrawable(ContextCompat.getDrawable(this, images.get(currentPostition)));
        }
    }

    private void changeImageNext() {
        currentPostition = currentPostition + 1;
        if (currentPostition < images.size()) {
            imageFullScreen.setImageDrawable(ContextCompat.getDrawable(this, images.get(currentPostition)));
        }
    }
}
