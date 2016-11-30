package tabfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import launchDetails.Config;
import pkapoor.wed.BlurBuilder;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class TheCouple extends Fragment {

    public static final String MyPREFERENCES = "myPreference";
    CountDownTimer mCountDownTimer;
    long marMilliSec, currentMilli, diff;
    TextView daysValue, HourValue, MinValue, SecValue,
            daysText, tvBriName, tvGroName, tvInviteText, tvTobeHeldOnHeading,
            tvBless, tvUs, tvTextblesss, tvDate;
    ImageView imageHea;
    private Animation tickmarkZoomIn, tickmarkzoomOutWithBounce;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.couple, container, false);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);

        Bitmap bb = BlurBuilder.blur(getContext(), bitmap);
        Drawable d = new BitmapDrawable(getResources(), bb);
        view.setBackground(d);
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        imageHea = (ImageView) view.findViewById(R.id.imageHea);

        tvGroName = (TextView) view.findViewById(R.id.GroName);
        tvBriName = (TextView) view.findViewById(R.id.BriName);
        tvInviteText = (TextView) view.findViewById(R.id.inviteText);
        tvTobeHeldOnHeading = (TextView) view.findViewById(R.id.tobeHeld);
        tvBless = (TextView) view.findViewById(R.id.tvBless);
        tvUs = (TextView) view.findViewById(R.id.tvUs);
        tvDate = (TextView) view.findViewById(R.id.Date);
        tvTextblesss = (TextView) view.findViewById(R.id.tcTextblesss);

        setAnimation();

        daysValue = (TextView) view.findViewById(R.id.daysValue);
        HourValue = (TextView) view.findViewById(R.id.HourValue);
        MinValue = (TextView) view.findViewById(R.id.MinValue);
        SecValue = (TextView) view.findViewById(R.id.SecValue);
        daysText = (TextView) view.findViewById(R.id.daysText);

        //Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DeliusSwashCaps-Regular.ttf");

        Typeface typeBriName = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bungasai.ttf");
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bungasai.ttf");

        tvGroName.setTypeface(typeBriName);
        tvBriName.setTypeface(typeBriName);

        tvInviteText.setTypeface(type);
        tvTobeHeldOnHeading.setTypeface(type);
        tvBless.setTypeface(type);
        tvUs.setTypeface(type);
        tvTextblesss.setTypeface(type);
        tvDate.setTypeface(type);

        fetchCoupleScreenValues();

        setTimer();
        return view;
    }

    private void fetchCoupleScreenValues() {

        tvGroName.setText(sharedPreferences.getString(Config.name_groom, "NAME"));
        tvBriName.setText(sharedPreferences.getString(Config.name_bride, "NAME"));
        tvTextblesss.setText(sharedPreferences.getString(Config.blessUs_para, "Bless Us"));
        try {
            setDateForMarriage(sharedPreferences.getString(Config.marriage_date, "NAME"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setDateForMarriage(String date) throws ParseException {
        Date d = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        int dayValue = cal.get(Calendar.DAY_OF_MONTH);
        int yearValue = cal.get(Calendar.YEAR);
        tvDate.setText(monthName + " " + dayValue + ", " + yearValue);
    }

    private void setAnimation() {
        tickmarkZoomIn = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_in_without_bounce);
        tickmarkzoomOutWithBounce = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_out_with_bounce);

        imageHea.setAnimation(tickmarkZoomIn);

        tickmarkZoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //On end of zoom,adding other animation.
                imageHea.setAnimation(tickmarkzoomOutWithBounce);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setTimer() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);

        String marTime = sharedPreferences.getString(Config.date_marriage, "22.01.2017, 19:00");
        // String marTime = "22.01.2017, 19:00";
        Date marDate;
        try {
            marDate = formatter.parse(marTime);
            marMilliSec = marDate.getTime();

            currentMilli = System.currentTimeMillis();
            diff = marMilliSec - currentMilli;

            mCountDownTimer = new CountDownTimer(diff, 1000) {
                @Override
                public void onFinish() {
                    daysValue.setText("0");
                    HourValue.setText("0");
                    MinValue.setText("0");
                    SecValue.setText("0");
                }

                @Override
                public void onTick(long millisUntilFinished) {

                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.DAYS.toMillis(days);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);


                    daysValue.setText("" + days);
                    HourValue.setText("" + hours);
                    MinValue.setText("" + minutes);
                    SecValue.setText("" + seconds);

                    if (days > 1)
                        daysText.setText("Days");
                    else {
                        daysText.setText("Day");
                    }
                }
            }.start();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
