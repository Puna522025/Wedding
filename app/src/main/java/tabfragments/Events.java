package tabfragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import launchDetails.Config;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class Events extends Fragment implements View.OnClickListener {

    RelativeLayout rlSagLocation, rlMarLocation,rlTextRec,rlTextMar, rlEventBack;
    TextView tvEvent, MarriEvent, dateValueSag, TimeValueSag, eventValueSag, dateValueMar, TimeValueMar, eventValueMar;
    CardView card1, card2;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.event, container, false);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_three);
//
//        //Bitmap bb = BlurBuilder.blur(getContext(), bitmap);
//        Drawable d = new BitmapDrawable(getResources(), bitmap);
//        view.setBackground(d);
        sharedPreferences = getActivity().getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);

        rlSagLocation = (RelativeLayout) view.findViewById(R.id.rlSagLocation);
        rlMarLocation = (RelativeLayout) view.findViewById(R.id.rlMarLocation);
        rlTextRec = (RelativeLayout) view.findViewById(R.id.rlTextRec);
        rlTextMar = (RelativeLayout) view.findViewById(R.id.rlTextMar);
        rlEventBack = (RelativeLayout) view.findViewById(R.id.rlEventBack);

        card1 = (CardView) view.findViewById(R.id.card1);
        card2 = (CardView) view.findViewById(R.id.card2);

        rlMarLocation.setOnClickListener(this);
        rlSagLocation.setOnClickListener(this);

        tvEvent = (TextView) view.findViewById(R.id.Event);
        MarriEvent = (TextView) view.findViewById(R.id.MarriEvent);

        //SAGAN
        dateValueSag = (TextView) view.findViewById(R.id.dateValue);
        TimeValueSag = (TextView) view.findViewById(R.id.TimeValue);
        eventValueSag = (TextView) view.findViewById(R.id.eventValue);
        //Mar
        dateValueMar = (TextView) view.findViewById(R.id.dateValueMar);
        TimeValueMar = (TextView) view.findViewById(R.id.TimeValueMar);
        eventValueMar = (TextView) view.findViewById(R.id.eventValueMar);

        changeTextColor();

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DeliusSwashCaps-Regular.ttf");

        MarriEvent.setTypeface(type);
        tvEvent.setTypeface(type);
        setEventTwoValues();
        setMarriageEventValues();
        return view;
    }

    private void changeTextColor() {
        String colorSelected = sharedPreferences.getString(Config.colorSelected,Integer.toString(R.color.colorRed));
        changeColor(colorSelected);
        if(sharedPreferences.getString(Config.back_image,"0").equalsIgnoreCase("0")){
            rlEventBack.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.back_seven));
        }
    }

    private void changeColor(String color) {

        int colorSelected = Integer.parseInt(color);
        rlTextRec.setBackgroundColor(ContextCompat.getColor(getContext(), colorSelected));
        rlTextMar.setBackgroundColor(ContextCompat.getColor(getContext(),colorSelected));
        //SAGAN
        dateValueSag.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        TimeValueSag.setTextColor(ContextCompat.getColor(getContext(),colorSelected));
        eventValueSag.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        //Mar
        dateValueMar.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        TimeValueMar.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        eventValueMar.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
    }

    private void setMarriageEventValues() {
        dateValueMar.setText(sharedPreferences.getString(Config.marriage_date, "DATE"));
        TimeValueMar.setText(sharedPreferences.getString(Config.marriage_time, "TIME"));
        eventValueMar.setText(sharedPreferences.getString(Config.marriage_location, "location"));
    }

    private void setEventTwoValues() {
        if (sharedPreferences.getString(Config.event_two_tobe, "true").equalsIgnoreCase("true")) {
            card1.setVisibility(View.VISIBLE);
            tvEvent.setText(sharedPreferences.getString(Config.event_two_name, "NAME"));
            dateValueSag.setText(sharedPreferences.getString(Config.event_Two_date, "DATE"));
            TimeValueSag.setText(sharedPreferences.getString(Config.event_Two_time, "TIME"));
            eventValueSag.setText(sharedPreferences.getString(Config.event_Two_location, "location"));
        }else{
            card1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlSagLocation:
                callMap("https://maps.google.com/maps?f=d&daddr="+eventValueSag.getText().toString());
                break;
            case R.id.rlMarLocation:
                callMap("https://maps.google.com/maps?f=d&daddr="+eventValueMar.getText().toString());
                break;
        }
    }

    private void callMap(String address) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(address));
        startActivity(intent);
    }
}
