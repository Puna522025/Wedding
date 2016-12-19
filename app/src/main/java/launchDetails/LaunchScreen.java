package launchDetails;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import analytics.AnalyticsApplication;
import database.DatabaseHandler;
import database.WedPojo;
import pkapoor.wed.FormDetails;
import pkapoor.wed.MainActivity;
import pkapoor.wed.R;
import viewlist.ShowList;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class LaunchScreen extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LaunchScreen";
    SharedPreferences sharedPreferences;
    DatabaseHandler database;
    TextView tvOR;
    private EditText etWedCode;
    private RelativeLayout rlCode, rlBackground, rlCreateInvite;
    private Button btnGetInvite, btnCreateInvite, btnGetList;
    private ProgressDialog progressDialog;
    private Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        etWedCode = (EditText) findViewById(R.id.etWedCode);
        rlCode = (RelativeLayout) findViewById(R.id.rlCode);
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
        rlCreateInvite = (RelativeLayout) findViewById(R.id.rlCreateInvite);
        btnGetInvite = (Button) findViewById(R.id.btnGetInvite);
        btnCreateInvite = (Button) findViewById(R.id.btnCreateInvite);
        btnGetList = (Button) findViewById(R.id.btnGetList);

        tvOR = (TextView) findViewById(R.id.tvOR);
        sharedPreferences = getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.two);
        Bitmap bb = BlurBuilder.blur(getApplicationContext(), bitmap);
        Drawable d = new BitmapDrawable(getResources(), bb);
       //
       // rlBackground.setBackground(d);*/
        rlBackground.setBackground(ContextCompat.getDrawable(this,R.drawable.two));
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        etWedCode.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.colorLightRed), PorterDuff.Mode.SRC_ATOP);
        etWedCode.setTextColor(ContextCompat.getColor(this, R.color.colorLightRed));
        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        final Animation grow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_anim);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
                rlCode.setVisibility(View.VISIBLE);
                rlCode.setAnimation(grow);
                rlCreateInvite.setVisibility(View.VISIBLE);
                rlCreateInvite.setAnimation(grow);

            }
        }, 1 * 200); // wait for 5 seconds
        btnGetInvite.setOnClickListener(this);
        btnCreateInvite.setOnClickListener(this);
        btnGetList.setOnClickListener(this);
        rlBackground.setOnClickListener(this);
        database = new DatabaseHandler(this);
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/Bungasai.ttf");

        btnGetList.setTypeface(type);

        btnCreateInvite.setTypeface(type);
        btnGetInvite.setTypeface(type);
        tvOR.setTypeface(type);
        getWindow().setStatusBarColor((ContextCompat.getColor(this, R.color.BlackStatusBar)));


        // Build GoogleApiClient with AppInvite API for receiving deep links
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();

        // Check if this app was launched from a deep link. Setting autoLaunchDeepLink to true
        // would automatically launch the deep link if one is found.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    if(!TextUtils.isEmpty(deepLink)) {
                                        String[] splitText = deepLink.split("utm_medium=");
                                        if (splitText.length > 1) {
                                            String inviteCode = splitText[1];
                                            etWedCode.setText(inviteCode);
                                            createDialog(inviteCode);
                                        }
                                    }
                                    } else {
                                    Log.d(TAG, "getInvitation: no deep link found.");
                                }
                            }
                        });

    }

    private void createDialog(final String inviteCode) {

        final Animation tickmarkZoomIn, tickmarkzoomOutWithBounce;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.launch_dialog_two);

        final Button btnGetInviteLaunch = (Button) dialog.findViewById(R.id.btnGetInvite);
        final TextView tvUniqueCode = (TextView) dialog.findViewById(R.id.tvUniqueCode);
        ImageView imageViewClose = (ImageView) dialog.findViewById(R.id.imageViewClose);

        TextView tvInvited = (TextView) dialog.findViewById(R.id.tvInvited);
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/Bungasai.ttf");

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

        tvInvited.setTypeface(type);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogDate;

        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);

        try {
            if (null != ((GradientDrawable) progressBar.getProgressDrawable())) {
                ((GradientDrawable) progressBar.getProgressDrawable()).
                        setColor(ContextCompat.getColor(getApplicationContext(), R.color.BlackToolBar));
            }
        } catch (Exception e) {
            Log.e("LaunchScreen", "Error while changing color" + e);
        }

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 600);
        animation.setDuration(2200); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        tvUniqueCode.setText(inviteCode);
        dialog.show();
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnGetInviteLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(inviteCode)) {
                    getInviteMethod();
                    dialog.dismiss();
                }else{
                    Toast.makeText(LaunchScreen.this, "No invite found..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etWedCode.setText(sharedPreferences.getString(Config.setLatestViewedId, ""));
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetInvite:
                if (!TextUtils.isEmpty(etWedCode.getText().toString())) {
                    getInviteMethod();
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

    private void getInviteMethod() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.MyPREFERENCES, MODE_PRIVATE);
        String latestViewedId = sharedPreferences.getString(Config.setLatestViewedId, "");
        // No need to hit the service if ID was already fetched the last time.
        if (!TextUtils.isEmpty(latestViewedId) &&
                latestViewedId.equalsIgnoreCase(etWedCode.getText().toString())&&
                latestViewedId.equalsIgnoreCase(sharedPreferences.getString(Config.unique_wed_code, ""))) {

            progressDialog.setMessage("Getting the invite..");
            progressDialog.show();
            saveInDBviewOnly();
            callMainActivity(latestViewedId);
        } else if (Config.isOnline(this)) {
            getDBweddingDetails();
        } else {
            new AlertDialog.Builder(LaunchScreen.this)
                    .setTitle("OOPS!!")
                    .setMessage("No Internet ..Please try again..")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void callMainActivity(String uniqueId) {
        Intent intent = new Intent(this, MainActivity.class);
        List<WedPojo> wedPojoArrayList = database.getAllWedDetails();
        if (null != wedPojoArrayList) {
            for (int i = 0; i < wedPojoArrayList.size(); i++) {
                if (wedPojoArrayList.get(i).getId().equalsIgnoreCase(uniqueId)
                        && wedPojoArrayList.get(i).getType().equalsIgnoreCase(Config.TYPE_WED_CREATED)) {
                    intent.putExtra(Config.setToolbarMenuIcons, Config.ONLY_SHARE);
                    break;
                }
            }
        }
        progressDialog.dismiss();
        startActivity(intent);
    }

    private void createInviteForm() {
        Intent intent = new Intent(this, FormDetails.class);
        intent.putExtra(Config.setToolbarMenuIcons, "yes");
        startActivity(intent);
    }

    private void getDBweddingDetails() {

        progressDialog.setMessage("Getting the invite..");
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
                Toast.makeText(LaunchScreen.this, "Oops ..Please try after sometime..", Toast.LENGTH_SHORT).show();
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
        String colorSelected = "";
        String back_image = "";

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

                colorSelected = weddingData.getString(Config.colorSelected);
                back_image = weddingData.getString(Config.back_image);

                SharedPreferences.Editor editor = getSharedPreferences(Config.MyPREFERENCES, MODE_PRIVATE).edit();
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

                editor.putString(Config.colorSelected, colorSelected);
                editor.putString(Config.back_image, back_image);

                editor.apply();
                saveInDBviewOnly();
                callMainActivity(unique_wed_code);

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
            Toast.makeText(LaunchScreen.this, "Oops ..Please try after sometime..", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInDBviewOnly() {
        if (!isWedExistsInDB()) {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
