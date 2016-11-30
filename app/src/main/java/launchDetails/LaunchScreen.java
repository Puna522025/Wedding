package launchDetails;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import pkapoor.wed.FormDetails;
import pkapoor.wed.MainActivity;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class LaunchScreen extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "myPreference";
    EditText etWedCode;
    RelativeLayout rlCode;
    Button btnGetInvite, btnCreateInvite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        etWedCode = (EditText) findViewById(R.id.etWedCode);
        rlCode = (RelativeLayout) findViewById(R.id.rlCode);
        btnGetInvite = (Button) findViewById(R.id.btnGetInvite);
        btnCreateInvite = (Button) findViewById(R.id.btnCreateInvite);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {
               /* Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                // close this activity
                finish();*/
                rlCode.setVisibility(View.VISIBLE);
            }
        }, 1 * 1500); // wait for 5 seconds
        btnGetInvite.setOnClickListener(this);
        btnCreateInvite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetInvite:
                if (!TextUtils.isEmpty(etWedCode.getText().toString())) {
                    getDBweddingDetails();
                } else {
                    Toast.makeText(this, "Please enter the wedding invite code", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnCreateInvite:
                createInviteForm();
                break;
        }
    }

    private void createInviteForm() {
        Intent intent = new Intent(this, FormDetails.class);
        intent.putExtra(Config.setToolbarMenuIcons,"yes");
        startActivity(intent);
    }

    private void getDBweddingDetails() {
        String url = Config.URL_FETCH + "'" + etWedCode.getText().toString() + "'";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        String sagan_engag_tobe = "";
        String sagan_date = "";
        String sagan_time = "";
        String sagan_location = "";
        String marriage_tobe = "";
        String marriage_date = "";
        String marriage_time = "";
        String marriage_location = "";
        String rsvp_tobe = "";
        String rsvp_name1 = "";
        String rsvp_name2 = "";
        String rsvp_phone_one = "";
        String rsvp_phone_two = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            unique_wed_code = collegeData.getString(Config.unique_wed_code);
            name_bride = collegeData.getString(Config.name_bride);
            name_groom = collegeData.getString(Config.name_groom);
            date_marriage = collegeData.getString(Config.date_marriage);
            blessUs_para = collegeData.getString(Config.blessUs_para);
            sagan_engag_tobe = collegeData.getString(Config.sagan_engag_tobe);
            sagan_date = collegeData.getString(Config.sagan_date);
            sagan_time = collegeData.getString(Config.sagan_time);
            sagan_location = collegeData.getString(Config.sagan_location);
            marriage_tobe = collegeData.getString(Config.marriage_tobe);
            marriage_date = collegeData.getString(Config.marriage_date);
            marriage_time = collegeData.getString(Config.marriage_time);
            marriage_location = collegeData.getString(Config.marriage_location);
            rsvp_tobe = collegeData.getString(Config.rsvp_tobe);
            rsvp_name1 = collegeData.getString(Config.rsvp_name1);
            rsvp_name2 = collegeData.getString(Config.rsvp_name2);
            rsvp_phone_one = collegeData.getString(Config.rsvp_phone_one);
            rsvp_phone_two = collegeData.getString(Config.rsvp_phone_two);

            SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
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
            editor.putString(Config.sagan_date, sagan_date);
            editor.putString(Config.sagan_engag_tobe, sagan_engag_tobe);
            editor.putString(Config.sagan_location, sagan_location);
            editor.putString(Config.sagan_time, sagan_time);
            editor.apply();

            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra(Config.setToolbarMenuIcons,"no");
            startActivity(intent);
           /* new AlertDialog.Builder(LaunchScreen.this)
                    .setTitle("Result")
                    .setMessage("unique_wed_code:\t" + unique_wed_code + "\nname_bride:\t" + name_bride + "\ndate_marriage:\t" + date_marriage
                            + "\nsagan_engag_tobe:\t" + sagan_engag_tobe + "\nrsvp_phone_one:\t" + rsvp_phone_one)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
