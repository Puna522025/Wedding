package pkapoor.wed;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import launchDetails.Config;

/**
 * Created by pkapo8 on 11/30/2016.
 */

public class FormDetails extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String MyPREFERENCES = "myPreference";
    ImageView imgDateCalender, imgDateCalenderEventTwo;
    int mYear, mMonth, mDay, mHour, mMinute;
    TextView tvmarDateTimeText, tvEventTwoDateTimeText, tvEventNameText, tvcontinueToInvite;

    EditText etEventNameText, etNameBri, etNameGro, etLocationValue, etPinCodeValue, etInviteMesValue,
            etLocationValueEventTwo, etPinCodeValueEventTwo, etRSVPNameOne, etRSVPMobileOne, etRSVPNameTwo, etRSVPMobileTwo, etInviteMesRSVPValue;

    RelativeLayout rlEventTwo, rlEventRSVP, rlBackground;
    SwitchCompat switchEventTwo, switchEventRSVP;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_details);

        initializingVariables();

        setSupportActionBar(toolbar);

        imgDateCalender.setOnClickListener(this);
        tvcontinueToInvite.setOnClickListener(this);
        imgDateCalenderEventTwo.setOnClickListener(this);
        switchEventTwo.setOnCheckedChangeListener(this);
        switchEventRSVP.setOnCheckedChangeListener(this);

        rlEventTwo.setVisibility(View.VISIBLE);
        tvEventNameText.setVisibility(View.VISIBLE);
        etEventNameText.setVisibility(View.VISIBLE);
        rlEventRSVP.setVisibility(View.VISIBLE);

        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        Bitmap bb = BlurBuilder.blur(this, bitmap);
        Drawable d = new BitmapDrawable(getResources(), bb);
        rlBackground.setBackground(d);*/
    }

    private void initializingVariables() {
        imgDateCalender = (ImageView) findViewById(R.id.imgDateCalender);
        imgDateCalenderEventTwo = (ImageView) findViewById(R.id.imgDateCalenderEventTwo);

        tvmarDateTimeText = (TextView) findViewById(R.id.marDateTimeText);
        tvEventTwoDateTimeText = (TextView) findViewById(R.id.EventTwoDateTimeText);
        tvEventNameText = (TextView) findViewById(R.id.EventNameText);
        tvcontinueToInvite = (TextView) findViewById(R.id.continueToInvite);

        rlEventTwo = (RelativeLayout) findViewById(R.id.rlEventTwo);
        rlEventRSVP = (RelativeLayout) findViewById(R.id.rlEventRSVP);
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);


        etNameBri = (EditText) findViewById(R.id.etNameBri);
        etNameGro = (EditText) findViewById(R.id.etNameGro);
        etLocationValue = (EditText) findViewById(R.id.LocationValue);
        etPinCodeValue = (EditText) findViewById(R.id.PinCodeValue);
        etInviteMesValue = (EditText) findViewById(R.id.InviteMesValue);

        etEventNameText = (EditText) findViewById(R.id.etEventNameText);
        etLocationValueEventTwo = (EditText) findViewById(R.id.LocationValueEventTwo);
        etPinCodeValueEventTwo = (EditText) findViewById(R.id.PinCodeValueEventTwo);

        etRSVPNameOne = (EditText) findViewById(R.id.etRSVPNameOne);
        etRSVPMobileOne = (EditText) findViewById(R.id.etRSVPMobileOne);
        etRSVPNameTwo = (EditText) findViewById(R.id.etRSVPNameTwo);
        etRSVPMobileTwo = (EditText) findViewById(R.id.etRSVPMobileTwo);
        etInviteMesRSVPValue = (EditText) findViewById(R.id.InviteMesRSVPValue);

        switchEventTwo = (SwitchCompat) findViewById(R.id.switchEventTwo);
        switchEventRSVP = (SwitchCompat) findViewById(R.id.switchEventRSVP);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDateCalender:
                setDate("wedding");
                break;
            case R.id.imgDateCalenderEventTwo:
                setDate("eventTwo");
                break;
            case R.id.continueToInvite:
                continueToInviteScreen();
                break;
        }
    }

    private void continueToInviteScreen() {
        //TODO: check if all the fields are filled properly or not..
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your invite..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
        editor.putString(Config.blessUs_para, etInviteMesValue.getText().toString());
        // editor.putString(Config.unique_wed_code, etInviteMesValue.getText().toString());
        String marDateTime = tvmarDateTimeText.getText().toString();

        if (!TextUtils.isEmpty(marDateTime)) {
            String marDate = marDateTime.substring(0, marDateTime.indexOf(","));
            String marTime = marDateTime.substring(marDateTime.indexOf(",") + 2, marDateTime.length());
            editor.putString(Config.date_marriage, marDateTime);
            editor.putString(Config.marriage_date, marDate);
            editor.putString(Config.marriage_time, marTime);
        }

        editor.putString(Config.marriage_location, etLocationValue.getText().toString() + "," + etPinCodeValue.getText().toString());
        editor.putString(Config.marriage_tobe, "true");
        editor.putString(Config.name_bride, etNameBri.getText().toString());
        editor.putString(Config.name_groom, etNameGro.getText().toString());
        editor.putString(Config.rsvp_name1, etRSVPNameOne.getText().toString());
        editor.putString(Config.rsvp_name2, etRSVPNameTwo.getText().toString());
        editor.putString(Config.rsvp_phone_one, etRSVPMobileOne.getText().toString());
        editor.putString(Config.rsvp_phone_two, etRSVPMobileTwo.getText().toString());
        editor.putString(Config.event_two_name, etEventNameText.getText().toString());
        editor.putString(Config.rsvp_text, etInviteMesRSVPValue.getText().toString());

        if (switchEventRSVP.isChecked()) {
            editor.putString(Config.rsvp_tobe, "true");
        } else if (!switchEventRSVP.isChecked()) {
            editor.putString(Config.rsvp_tobe, "false");
        }

        String eventTwoDateTime = tvEventTwoDateTimeText.getText().toString();
        if (!TextUtils.isEmpty(eventTwoDateTime)) {
            String eventTwoDate = eventTwoDateTime.substring(0, eventTwoDateTime.indexOf(","));
            String eventTwoTime = eventTwoDateTime.substring(eventTwoDateTime.indexOf(",") + 2, eventTwoDateTime.length());
            editor.putString(Config.event_Two_date, eventTwoDate);
            editor.putString(Config.event_Two_time, eventTwoTime);
        }

        if (switchEventTwo.isChecked()) {
            editor.putString(Config.event_two_tobe, "true");
        } else if (!switchEventTwo.isChecked()) {
            editor.putString(Config.event_two_tobe, "false");
        }

        editor.putString(Config.event_Two_location, etLocationValueEventTwo.getText().toString() + "," + etPinCodeValueEventTwo.getText().toString());
        editor.apply();

        progressDialog.dismiss();

        Intent intentOne = getIntent();
        Bundle extras = intentOne.getExtras();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Config.setToolbarMenuIcons, extras.get(Config.setToolbarMenuIcons).toString());
        startActivity(intent);
    }

    private void setDate(final String type) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear = view.getYear() - 1900;
                        mMonth = view.getMonth();
                        mDay = view.getDayOfMonth();
                        showTimePicker(type);
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.getWindow().getAttributes().windowAnimations = R.style.DialogDate;
        dpd.show();
    }

    private void showTimePicker(final String type) {
        final TimePickerDialog tpd = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker viewTime, int hourOfDay,
                                          int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        SimpleDateFormat sdf;
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        cal.clear();
                        sdf = new SimpleDateFormat("yyyy");
                        int formatedyear = Integer.parseInt(sdf.format(new Date(mYear, mMonth, mDay)));
                        sdf = new SimpleDateFormat("MM");
                        int formatedmonth = Integer.parseInt(sdf.format(new Date(mYear, mMonth, mDay)));
                        int month = formatedmonth - 1;
                        cal.set(formatedyear, month, mDay, mHour, mMinute, 0);

                        Calendar calSystem = Calendar.getInstance();
                        calSystem.setTimeInMillis(System.currentTimeMillis());

                        if (calSystem.getTimeInMillis() > cal.getTimeInMillis()) {
                            checkTime();
                        } else {
                            mHour = hourOfDay;
                            mMinute = minute;
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
                            String formatedDate = sdf1.format(new Date(mYear, mMonth, mDay));
                            if (type.equalsIgnoreCase("wedding")) {
                                tvmarDateTimeText.setVisibility(View.VISIBLE);
                                updateTime(mHour, mMinute, tvmarDateTimeText, formatedDate);
                            } else if (type.equalsIgnoreCase("eventTwo")) {
                                tvEventTwoDateTimeText.setVisibility(View.VISIBLE);
                                updateTime(mHour, mMinute, tvEventTwoDateTimeText, formatedDate);
                            }
                        }
                    }
                }
                , mHour, mMinute, false);

        tpd.getWindow().getAttributes().windowAnimations = R.style.DialogTime;
        tpd.show();
    }

    private void checkTime() {
        Toast.makeText(this, "Oops!!Invalid Time..", Toast.LENGTH_SHORT).show();
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins, TextView marDateTimeText, String formatedDate) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        marDateTimeText.setText(formatedDate + ", " + aTime);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()) {
            case R.id.switchEventTwo:
                if (compoundButton.isChecked()) {
                    rlEventTwo.setVisibility(View.VISIBLE);
                    tvEventNameText.setVisibility(View.VISIBLE);
                    etEventNameText.setVisibility(View.VISIBLE);
                } else {
                    rlEventTwo.setVisibility(View.GONE);
                    tvEventNameText.setVisibility(View.GONE);
                    etEventNameText.setVisibility(View.GONE);
                }
                break;
            case R.id.switchEventRSVP:
                if (compoundButton.isChecked()) {
                    rlEventRSVP.setVisibility(View.VISIBLE);
                } else {
                    rlEventRSVP.setVisibility(View.GONE);
                }
                break;
        }
    }
}
