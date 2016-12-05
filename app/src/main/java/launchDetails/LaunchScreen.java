package launchDetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import database.DatabaseHandler;
import database.WedPojo;
import pkapoor.wed.FormDetails;
import pkapoor.wed.MainActivity;
import pkapoor.wed.R;
import viewlist.ShowList;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class LaunchScreen extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "myPreference";
    private EditText etWedCode;
    private RelativeLayout rlCode, rlBackground;
    private Button btnGetInvite, btnCreateInvite, btnGetList;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    DatabaseHandler database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        etWedCode = (EditText) findViewById(R.id.etWedCode);
        rlCode = (RelativeLayout) findViewById(R.id.rlCode);
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);

        imageView = (ImageView) findViewById(R.id.imageView);

        btnGetInvite = (Button) findViewById(R.id.btnGetInvite);
        btnCreateInvite = (Button) findViewById(R.id.btnCreateInvite);
        btnGetList = (Button) findViewById(R.id.btnGetList);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        etWedCode.setText(sharedPreferences.getString(Config.setLatestViewedId, ""));

        etWedCode.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.colorLightRed), PorterDuff.Mode.SRC_ATOP);
        etWedCode.setTextColor(ContextCompat.getColor(this, R.color.colorLightRed));
        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        final Animation grow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_anim);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                imageView.setAnimation(slideUp);
                rlCode.setVisibility(View.VISIBLE);
                rlCode.setAnimation(grow);

            }
        }, 1 * 900); // wait for 5 seconds
        btnGetInvite.setOnClickListener(this);
        btnCreateInvite.setOnClickListener(this);
        btnGetList.setOnClickListener(this);
        rlBackground.setOnClickListener(this);
        database = new DatabaseHandler(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetInvite:
                if (!TextUtils.isEmpty(etWedCode.getText().toString())) {
                    SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    String latestViewedId = sharedPreferences.getString(Config.setLatestViewedId,"");
                    // No need to hit the service if ID was already fetched the last time.
                    if(!TextUtils.isEmpty(latestViewedId)&&latestViewedId.equalsIgnoreCase(etWedCode.getText().toString())){
                        callMainActivity();
                    }
                    else if (Config.isOnline(this)) {
                        getDBweddingDetails();
                    } else {
                        new AlertDialog.Builder(LaunchScreen.this)
                                .setTitle("OOPS!!")
                                .setMessage("No Internet ..try again..")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } else {
                    Toast.makeText(this, "Please enter the wedding invite code", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnCreateInvite:
                createInviteForm();
                break;
            case R.id.rlBackground:
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                break;
            case R.id.btnGetList:
                Intent intent = new Intent(this, ShowList.class);
                startActivity(intent);
                break;
        }
    }

    private void callMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Config.setToolbarMenuIcons, "no");
        startActivity(intent);
    }

    private void createInviteForm() {
        Intent intent = new Intent(this, FormDetails.class);
        intent.putExtra(Config.setToolbarMenuIcons, "yes");
        startActivity(intent);
    }

    private void getDBweddingDetails() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting the invite..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Config.URL_FETCH + "'" + etWedCode.getText().toString() + "'";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LaunchScreen.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        String unique_wed_code = "";
        String name_bride = "";
        String name_groom = "";
        String date_marriage = "";
        String blessUs_para = "";
        String event_Two_tobe = "";
        String event_Two_date = "";
        String event_Two_time = "";
        String event_Two_location = "";
        String marriage_tobe = "";
        String marriage_date = "";
        String marriage_time = "";
        String marriage_location = "";
        String rsvp_tobe = "";
        String rsvp_name1 = "";
        String rsvp_name2 = "";
        String rsvp_phone_one = "";
        String rsvp_phone_two = "";
        String event_two_Name = "";
        String rsvp_text = "";

        try {
            progressDialog.setMessage("Creating you invite..");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject weddingData = result.getJSONObject(0);
            unique_wed_code = weddingData.getString(Config.unique_wed_code);
            if (!unique_wed_code.equalsIgnoreCase("null")) {
                name_bride = weddingData.getString(Config.name_bride);
                name_groom = weddingData.getString(Config.name_groom);
                date_marriage = weddingData.getString(Config.date_marriage);
                blessUs_para = weddingData.getString(Config.blessUs_para);
                event_Two_tobe = weddingData.getString(Config.event_two_tobe);
                event_Two_date = weddingData.getString(Config.event_Two_date);
                event_Two_time = weddingData.getString(Config.event_Two_time);
                event_Two_location = weddingData.getString(Config.event_Two_location);
                marriage_tobe = weddingData.getString(Config.marriage_tobe);
                marriage_date = weddingData.getString(Config.marriage_date);
                marriage_time = weddingData.getString(Config.marriage_time);
                marriage_location = weddingData.getString(Config.marriage_location);
                rsvp_tobe = weddingData.getString(Config.rsvp_tobe);
                rsvp_name1 = weddingData.getString(Config.rsvp_name1);
                rsvp_name2 = weddingData.getString(Config.rsvp_name2);
                rsvp_phone_one = weddingData.getString(Config.rsvp_phone_one);
                rsvp_phone_two = weddingData.getString(Config.rsvp_phone_two);
                event_two_Name = weddingData.getString(Config.event_two_name);
                rsvp_text = weddingData.getString(Config.rsvp_text);

                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                editor.putString(Config.setLatestViewedId, etWedCode.getText().toString());
                editor.putString(Config.blessUs_para, blessUs_para);
                editor.putString(Config.unique_wed_code, unique_wed_code);
                editor.putString(Config.date_marriage, date_marriage);
                editor.putString(Config.marriage_date, marriage_date);
                editor.putString(Config.marriage_location, marriage_location);
                editor.putString(Config.marriage_time, marriage_time);
                editor.putString(Config.marriage_tobe, marriage_tobe);
                editor.putString(Config.name_bride, name_bride);
                editor.putString(Config.name_groom, name_groom);
                editor.putString(Config.rsvp_name1, rsvp_name1);
                editor.putString(Config.rsvp_name2, rsvp_name2);
                editor.putString(Config.rsvp_phone_one, rsvp_phone_one);
                editor.putString(Config.rsvp_phone_two, rsvp_phone_two);
                editor.putString(Config.rsvp_tobe, rsvp_tobe);
                editor.putString(Config.event_Two_date, event_Two_date);
                editor.putString(Config.event_two_tobe, event_Two_tobe);
                editor.putString(Config.event_Two_location, event_Two_location);
                editor.putString(Config.event_Two_time, event_Two_time);
                editor.putString(Config.event_two_name, event_two_Name);
                editor.putString(Config.rsvp_text, rsvp_text);
                editor.apply();
                saveInDBviewOnly();
                progressDialog.dismiss();

                callMainActivity();

            } else {
                progressDialog.dismiss();
                new AlertDialog.Builder(LaunchScreen.this)
                        .setTitle("OOPS!!")
                        .setMessage("Sorry....There seems to be no wedding with this code. Please try again..")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        } catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void saveInDBviewOnly() {
        if (!isWedExistsInDB())
        {
            WedPojo wedPojo = new WedPojo();
            String brideName = sharedPreferences.getString(Config.name_bride, "");
            String groomName = sharedPreferences.getString(Config.name_groom, "");

            wedPojo.setName(brideName + " & " + groomName);
            wedPojo.setType(Config.TYPE_WED_VIEWED);
            wedPojo.setDate(sharedPreferences.getString(Config.marriage_date, ""));
            wedPojo.setId(sharedPreferences.getString(Config.unique_wed_code, ""));

            database.addWedDetails(wedPojo);
        }
    }

    private boolean isWedExistsInDB() {
        List<WedPojo> wedPojoArrayList = database.getAllWedDetails();
        for (int i = 0; i < wedPojoArrayList.size(); i++) {
            if (wedPojoArrayList.get(i).getId().equalsIgnoreCase(sharedPreferences.getString(Config.unique_wed_code, ""))) {
                return true;
            }
        }
        return false;
    }
}
