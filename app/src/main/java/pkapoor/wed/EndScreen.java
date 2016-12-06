package pkapoor.wed;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import launchDetails.Config;
import launchDetails.LaunchScreen;

/**
 * Created by pkapo8 on 12/1/2016.
 */

public class EndScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvUniqueCode, tvCongrats, tvYourCodeText, tvShare,tvContinue;
    RelativeLayout rlShare,rlEndBackground;
    private Animation tickmarkZoomIn, tickmarkzoomOutWithBounce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        rlShare = (RelativeLayout) findViewById(R.id.rlShare);
        rlEndBackground = (RelativeLayout) findViewById(R.id.rlEndBackground);

        tvUniqueCode = (TextView) findViewById(R.id.tvUniqueCode);
        tvContinue = (TextView) findViewById(R.id.tvContinue);
        tvCongrats = (TextView) findViewById(R.id.tvCongrats);
        tvYourCodeText = (TextView) findViewById(R.id.tvYourCodeText);
        tvShare = (TextView) findViewById(R.id.tvShare);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Bungasai.ttf");
        tvCongrats.setTypeface(type);
        tvYourCodeText.setTypeface(type);
        tvShare.setTypeface(type);
        tvContinue.setTypeface(type);

        changeBackgroundColor();

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().get("uniqueCode") != null) {
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
        rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.shareIntent(EndScreen.this);
            }
        });
    }

    private void changeBackgroundColor() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.MyPREFERENCES, MODE_PRIVATE);
        String color = sharedPreferences.getString(Config.colorSelected,Integer.toString(R.color.colorRed));

        int colorSelected = Integer.parseInt(color);
        rlEndBackground.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), colorSelected));
        tvUniqueCode.setTextColor(ContextCompat.getColor(getApplicationContext(),colorSelected));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor((ContextCompat.getColor(getApplicationContext(), colorSelected)));
    }

    @Override
    public void onBackPressed() {
    }
}
