package viewlist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import analytics.AnalyticsApplication;
import database.DatabaseHandler;
import database.WedPojo;
import launchDetails.Config;
import pkapoor.wed.MainActivity;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 12/2/2016.
 */

public class ShowList extends AppCompatActivity {

    DatabaseHandler database;
    RecyclerView listCreatedWed, listViewedWed;
    ArrayList<WedPojo> wedPojosCreatedWed, wedPojosViewedWed;
    AdapterListWed adapterListWed,adapterListViewed;
    private ProgressDialog progressDialog;
    TextView tvNoInvitesFound;
    RelativeLayout rlBack;
    private static final String TAG = "ShowList";
    private Tracker mTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_viewed_list);
        database = new DatabaseHandler(this);

        rlBack = (RelativeLayout)findViewById(R.id.rlBack);
       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_splash);
        Bitmap bb = BlurBuilder.blur(getApplicationContext(), bitmap);
        Drawable d = new BitmapDrawable(getResources(), bb);
        rlBack.setBackground(d);*/

        listCreatedWed = (RecyclerView) findViewById(R.id.listCreatedWed);
        listViewedWed = (RecyclerView) findViewById(R.id.listViewedWed);

        tvNoInvitesFound = (TextView) findViewById(R.id.tvNoInvitesFound);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting the details..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        wedPojosCreatedWed = new ArrayList<>();
        wedPojosViewedWed = new ArrayList<>();

        List<WedPojo> wedPojos = database.getAllWedDetails();
        if(wedPojos.size()>0) {
            tvNoInvitesFound.setVisibility(View.GONE);
            for (int i = 0; i < wedPojos.size(); i++) {
                if (wedPojos.get(i).getType().equalsIgnoreCase(Config.TYPE_WED_CREATED)) {
                    wedPojosCreatedWed.add(wedPojos.get(i));
                } else if (wedPojos.get(i).getType().equalsIgnoreCase(Config.TYPE_WED_VIEWED)) {
                    wedPojosViewedWed.add(wedPojos.get(i));
                }
            }
        }else{
            tvNoInvitesFound.setVisibility(View.VISIBLE);
        }
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setListLayout(listCreatedWed);
        setListLayout(listViewedWed);

        adapterListWed = new AdapterListWed(wedPojosCreatedWed, this);
        listCreatedWed.setAdapter(adapterListWed);

        adapterListViewed = new AdapterListWed(wedPojosViewedWed, this);
        listViewedWed.setAdapter(adapterListViewed);
        progressDialog.dismiss();
        ((AdapterListWed) adapterListWed).setOnItemClickListener(new AdapterListWed.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                TextView tvId = (TextView) v.findViewById(R.id.tvId);
                TextView tvType = (TextView) v.findViewById(R.id.tvType);
                if (Config.isOnline(ShowList.this)) {
                    getDBweddingDetails(tvId.getText().toString(),tvType.getText().toString());
                } else {
                    new AlertDialog.Builder(ShowList.this)
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

                Toast.makeText(ShowList.this, tvId.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void getDBweddingDetails(final String unique_code, final String type) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting the invite..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Config.URL_FETCH + "'" + unique_code + "'";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response,unique_code,type);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               /* if(null != error){
                    Toast.makeText(ShowList.this, "error -"+ error, Toast.LENGTH_LONG).show();
                }else{*/
                    Toast.makeText(ShowList.this, "Error..Please try after some time..", Toast.LENGTH_LONG).show();
            //    }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response, String unique_code, String type) {

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
                editor.putString(Config.setLatestViewedId, unique_code);
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
                progressDialog.dismiss();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Config.setToolbarMenuIcons, type);
                startActivity(intent);

            } else {
                progressDialog.dismiss();
                new AlertDialog.Builder(ShowList.this)
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
    private void setListLayout(RecyclerView list) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
    }
}
