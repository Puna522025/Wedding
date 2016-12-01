package pkapoor.wed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import launchDetails.Config;
import launchDetails.LaunchScreen;
import tabfragments.Events;
import tabfragments.Gallery;
import tabfragments.TheCouple;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String MyPREFERENCES = "myPreference";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String uniqueCode = "";
    private int[] tabIcons;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        setupTabIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras.get(Config.setToolbarMenuIcons).toString().equalsIgnoreCase("yes")) {
            getMenuInflater().inflate(R.menu.menu_create_invite, menu);
        } else if (extras.get(Config.setToolbarMenuIcons).toString().equalsIgnoreCase("no")) {
            getMenuInflater().inflate(R.menu.menu_empty, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveDatatoDB();
            return true;
        }
        if (id == R.id.action_edit) {
            finish();
            //backgroundColorPickerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDatatoDB() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Create Invite")
                .setMessage("You will not be able to edit this invite if you save it.Do you want to create this invite?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        insertToDatabase();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void insertToDatabase() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Saving the invite..");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    HashMap<String, String> paramsj = new HashMap<>();

                    String brideName = sharedPreferences.getString(Config.name_bride, "NAME");
                    String groomName = sharedPreferences.getString(Config.name_groom, "NAME");

                    String brideInitial = brideName.substring(0, 1);
                    String groomInitial = groomName.substring(0, 1);

                    uniqueCode = brideInitial + groomInitial + ((int) (Math.random() * 999) + 100);

                    paramsj.put(Config.unique_wed_code, uniqueCode);
                    paramsj.put(Config.name_bride, brideName);
                    paramsj.put(Config.name_groom, groomName);
                    paramsj.put(Config.date_marriage, sharedPreferences.getString(Config.date_marriage, "DATE"));
                    paramsj.put(Config.blessUs_para, sharedPreferences.getString(Config.blessUs_para, "NAME"));
                    paramsj.put(Config.event_two_tobe, sharedPreferences.getString(Config.event_two_tobe, "NAME"));
                    paramsj.put(Config.event_Two_date, sharedPreferences.getString(Config.event_Two_date, "NAME"));
                    paramsj.put(Config.event_Two_time, sharedPreferences.getString(Config.event_Two_time, "NAME"));
                    paramsj.put(Config.event_Two_location, sharedPreferences.getString(Config.event_Two_location, "NAME"));
                    paramsj.put(Config.marriage_tobe, sharedPreferences.getString(Config.marriage_tobe, "NAME"));
                    paramsj.put(Config.marriage_date, sharedPreferences.getString(Config.marriage_date, "NAME"));
                    paramsj.put(Config.marriage_time, sharedPreferences.getString(Config.marriage_time, "NAME"));
                    paramsj.put(Config.marriage_location, sharedPreferences.getString(Config.marriage_location, "NAME"));
                    paramsj.put(Config.rsvp_tobe, sharedPreferences.getString(Config.rsvp_tobe, "NAME"));
                    paramsj.put(Config.rsvp_name1, sharedPreferences.getString(Config.rsvp_name1, "NAME"));
                    paramsj.put(Config.rsvp_name2, sharedPreferences.getString(Config.rsvp_name2, "NAME"));
                    paramsj.put(Config.rsvp_phone_one, sharedPreferences.getString(Config.rsvp_phone_one, "Phone"));
                    paramsj.put(Config.rsvp_phone_two, sharedPreferences.getString(Config.rsvp_phone_two, "Phone"));
                    paramsj.put(Config.event_two_name, sharedPreferences.getString(Config.event_two_name, "NAME"));

                    StringBuilder sb = new StringBuilder();
                    URL url = new URL("http://vnnps.esy.es/insert-db.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();

                    //Writing parameters to the request
                    //We are using a method getPostDataString which is defined below
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(paramsj));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuilder();
                        String response;
                        //Reading server response
                        while ((response = br.readLine()) != null) {
                            sb.append(response);
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Result")
                        .setMessage("Unique Code - " + uniqueCode)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, LaunchScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

   /* public interface MyPagerSwitchListener {
        void onPageScrolled(int position);
    }

    public void setOnItemClickListener(MyPagerSwitchListener myClickListener) {
        this.myPagerSwitchListener = myClickListener;
    }
*/

    private void setupTabIcons() {
        Drawable drawableCouple = getDrawable(R.drawable.the_couple);
        drawableCouple.setTint(ContextCompat.getColor(this, android.R.color.white));

        Drawable drawableEvents = getDrawable(R.drawable.calender_one);
        drawableEvents.setTint(ContextCompat.getColor(this, android.R.color.white));

        Drawable drawableLocation = getDrawable(R.drawable.location);
        drawableLocation.setTint(ContextCompat.getColor(this, android.R.color.white));


        tabLayout.getTabAt(0).setIcon(drawableCouple);
        tabLayout.getTabAt(1).setIcon(drawableEvents);
        tabLayout.getTabAt(2).setIcon(drawableLocation);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TheCouple(), "The Couple");
        adapter.addFragment(new Events(), "Events");
        adapter.addFragment(new Gallery(), "Gallery");
        viewPager.setAdapter(adapter);
    }
}
