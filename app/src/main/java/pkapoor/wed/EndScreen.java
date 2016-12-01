package pkapoor.wed;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import launchDetails.LaunchScreen;

/**
 * Created by pkapo8 on 12/1/2016.
 */

public class EndScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvUniqueCode,tvContinue;
    private Animation tickmarkZoomIn, tickmarkzoomOutWithBounce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvUniqueCode = (TextView) findViewById(R.id.tvUniqueCode);
        tvContinue = (TextView) findViewById(R.id.tvContinue);
        if (getIntent() != null && getIntent().getExtras().get("uniqueCode") != null) {
            tvUniqueCode.setText(getIntent().getExtras().get("uniqueCode").toString());
        }
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 600);
        animation.setDuration(2200); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        tickmarkZoomIn = AnimationUtils.loadAnimation(this,
                R.anim.zoom_in_without_bounce);
        tickmarkzoomOutWithBounce = AnimationUtils.loadAnimation(this,
                R.anim.zoom_out_with_bounce);

        tvUniqueCode.setAnimation(tickmarkZoomIn);

        tickmarkZoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //On end of zoom,adding other animation.
                tvUniqueCode.setAnimation(tickmarkzoomOutWithBounce);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LaunchScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}