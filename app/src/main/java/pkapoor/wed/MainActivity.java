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
import android.text.TextUtils;
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

import database.DatabaseHandler;
import database.WedPojo;
import launchDetails.Config;
import tabfragments.Events;
import tabfragments.Gallery;
import tabfragments.TheCouple;
import viewlist.ShowList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String uniqueCode = "";
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(Config.MyPREFERENCES, Context.MODE_PRIVATE);
        setThemeColor(sharedPreferences.getString(Config.colorSelected,"colorRed"));

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Getting the invite..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        database = new DatabaseHandler(this);
        changeToolBarTheme();
        setupTabIcons();
        progressDialog.dismiss();
    }

    private void changeToolBarTheme() {
        String colorSelected = sharedPreferences.getString(Config.colorSelected,"colorRed");
        if(colorSelected.equalsIgnoreCase("colorRed")){
            Config.changeTheme(R.color.colorPrimary,R.color.colorPrimaryDark,toolbar,this,getWindow(),tabLayout);
        }else if(colorSelected.equalsIgnoreCase("PinkKittyToolBar")){
            Config.changeTheme(R.color.PinkKittyToolBar,R.color.PinkKittyStatusBar,toolbar,this,getWindow(),tabLayout);
        }else if(colorSelected.equalsIgnoreCase("GreenToolBar")){
            Config.changeTheme(R.color.GreenToolBar,R.color.GreenStatusBar,toolbar,this,getWindow(),tabLayout);
        }else if(colorSelected.equalsIgnoreCase("BlackToolBar")){
            Config.changeTheme(R.color.BlackToolBar,R.color.BlackStatusBar,toolbar,this,getWindow(),tabLayout);
        }else if(colorSelected.equalsIgnoreCase("BlueToolBar")){
            Config.changeTheme(R.color.BlueToolBar,R.color.BlueStatusBar,toolbar,this,getWindow(),tabLayout);
        }
    }

    private void setThemeColor(String colorSelected) {
        if(colorSelected.equalsIgnoreCase("colorRed")){
            getTheme().applyStyle(R.style.MyMaterialTheme,true);
        }else if(colorSelected.equalsIgnoreCase("PinkKittyToolBar")){
            getTheme().applyStyle(R.style.PinkTheme,true);
        }else if(colorSelected.equalsIgnoreCase("GreenToolBar")){
            getTheme().applyStyle(R.style.GreenTheme,true);
        }else if(colorSelected.equalsIgnoreCase("BlackToolBar")){
            getTheme().applyStyle(R.style.BlackTheme,true);
        }else if(colorSelected.equalsIgnoreCase("BlueToolBar")){
            getTheme().applyStyle(R.style.BlueTheme,true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (null != extras && null != extras.get(Config.setToolbarMenuIcons)) {
            if (extras.get(Config.setToolbarMenuIcons).toString().equalsIgnoreCase("yes")) {
                getMenuInflater().inflate(R.menu.menu_create_invite, menu);
            } else if (extras.get(Config.setToolbarMenuIcons).toString().equalsIgnoreCase(Config.ONLY_SHARE)) {
                getMenuInflater().inflate(R.menu.menu_view_type, menu);
                MenuItem action_delete = menu.findItem(R.id.action_delete);
                action_delete.setVisible(false);
            } else if (extras.get(Config.setToolbarMenuIcons).toString().equalsIgnoreCase(Config.TYPE_WED_CREATED)) {
                getMenuInflater().inflate(R.menu.menu_view_type, menu);
            } else if (extras.get(Config.setToolbarMenuIcons).toString().equalsIgnoreCase(Config.TYPE_WED_VIEWED)) {
                getMenuInflater().inflate(R.menu.menu_view_type, menu);
                MenuItem action_share = menu.findItem(R.id.action_share);
                action_share.setVisible(false);
            } else {
                getMenuInflater().inflate(R.menu.menu_empty, menu);
            }
        } else {
            getMenuInflater().inflate(R.menu.menu_empty, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (Config.isOnline(MainActivity.this)) {
                saveDatatoDB();
            }else {
                new AlertDialog.Builder(MainActivity.this)
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
            return true;
        }
        if (id == R.id.action_edit) {
            finish();
            return true;
        }
        if (id == R.id.action_share) {
            Config.shareIntent(MainActivity.this, sharedPreferences.getString(Config.unique_wed_code,""));
           /* Intent intent = new AppInviteInvitation.IntentBuilder("hello")
                    .setMessage("hello message")
                    .setDeepLink(Uri.parse("http://play.google.com/store/apps/details?id=sample.myapplication"))
                    .setCallToActionText("ct253")
                    .build();
            startActivityForResult(intent, 1);*/
            return true;
        }
        if (id == R.id.action_delete) {
            deleteEntry();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteEntry() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Invite")
                .setMessage("Are you sure you want to delete this invite?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        database.deleteNote(sharedPreferences.getString(Config.unique_wed_code, ""));
                        finish();
                        Intent intent = new Intent(MainActivity.this, ShowList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_delete)
                .show();

    }

    private void saveDatatoDB() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Create Invite")
                .setMessage("You will not be able to edit this invite if you save it. Do you want to create this invite?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        insertToOnlineDatabase();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_save)
                .show();
    }

    private void insertToOnlineDatabase() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Saving the invite..");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    HashMap<String, String> paramsj = new HashMap<>();

                    String brideName = sharedPreferences.getString(Config.name_bride, "");
                    String groomName = sharedPreferences.getString(Config.name_groom, "");
                    if (!TextUtils.isEmpty(brideName) && !TextUtils.isEmpty(groomName)) {
                        String brideInitial = brideName.substring(0, 1);
                        String groomInitial = groomName.substring(0, 1);

                        uniqueCode = brideInitial + groomInitial + ((int) (Math.random() * 999) + 100);
                    }
                    sharedPreferences.edit().putString(Config.unique_wed_code,uniqueCode).commit();
                    paramsj.put(Config.unique_wed_code, uniqueCode);
                    paramsj.put(Config.name_bride, brideName);
                    paramsj.put(Config.name_groom, groomName);
                    paramsj.put(Config.date_marriage, sharedPreferences.getString(Config.date_marriage, "DATE"));
                    paramsj.put(Config.blessUs_para, sharedPreferences.getString(Config.blessUs_para, "NAME"));
                    paramsj.put(Config.event_two_tobe, sharedPreferences.getString(Config.event_two_tobe, "false"));
                    paramsj.put(Config.event_Two_date, sharedPreferences.getString(Config.event_Two_date, "NAME"));
                    paramsj.put(Config.event_Two_time, sharedPreferences.getString(Config.event_Two_time, "NAME"));
                    paramsj.put(Config.event_Two_location, sharedPreferences.getString(Config.event_Two_location, "NAME"));
                    paramsj.put(Config.marriage_tobe, sharedPreferences.getString(Config.marriage_tobe, "NAME"));
                    paramsj.put(Config.marriage_date, sharedPreferences.getString(Config.marriage_date, "NAME"));
                    paramsj.put(Config.marriage_time, sharedPreferences.getString(Config.marriage_time, "NAME"));
                    paramsj.put(Config.marriage_location, sharedPreferences.getString(Config.marriage_location, "NAME"));
                    paramsj.put(Config.rsvp_tobe, sharedPreferences.getString(Config.rsvp_tobe, "false"));
                    paramsj.put(Config.rsvp_name1, sharedPreferences.getString(Config.rsvp_name1, "NAME"));
                    paramsj.put(Config.rsvp_name2, sharedPreferences.getString(Config.rsvp_name2, "NAME"));
                    paramsj.put(Config.rsvp_phone_one, sharedPreferences.getString(Config.rsvp_phone_one, ""));
                    paramsj.put(Config.rsvp_phone_two, sharedPreferences.getString(Config.rsvp_phone_two, ""));
                    paramsj.put(Config.event_two_name, sharedPreferences.getString(Config.event_two_name, "NAME"));
                    paramsj.put(Config.rsvp_text, sharedPreferences.getString(Config.rsvp_text, "NAME"));

                    paramsj.put(Config.colorSelected, sharedPreferences.getString(Config.colorSelected, "colorRed"));
                    paramsj.put(Config.back_image,sharedPreferences.getString(Config.back_image, "0"));

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
                //progressDialog.dismiss();
                if (!TextUtils.isEmpty(result) && result.equalsIgnoreCase("success")) {
                    savingInDB();
                    Intent intent = new Intent(MainActivity.this, EndScreen.class);
                    intent.putExtra("uniqueCode", uniqueCode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Result")
                            .setMessage(result + "Unable to save details.Please try after some time.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private void savingInDB() {
        WedPojo wedPojo = new WedPojo();
        String brideName = sharedPreferences.getString(Config.name_bride, "");
        String groomName = sharedPreferences.getString(Config.name_groom, "");

        wedPojo.setName(brideName + " & " + groomName);
        wedPojo.setType(Config.TYPE_WED_CREATED);
        wedPojo.setDate(sharedPreferences.getString(Config.marriage_date, ""));
        wedPojo.setId(uniqueCode);

        database.addWedDetails(wedPojo);
        progressDialog.dismiss();
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

        tabLayout.getTabAt(0).setIcon(drawableCouple);
        tabLayout.getTabAt(1).setIcon(drawableEvents);
        if(sharedPreferences.getString(Config.rsvp_tobe,"false").equalsIgnoreCase("true")) {
            Drawable drawableRSVP = getDrawable(R.drawable.rsvp);
            drawableRSVP.setTint(ContextCompat.getColor(this, android.R.color.white));
            tabLayout.getTabAt(2).setIcon(drawableRSVP);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TheCouple(), "The Couple");
        adapter.addFragment(new Events(), "Events");
        if(sharedPreferences.getString(Config.rsvp_tobe,"false").equalsIgnoreCase("true")) {
            adapter.addFragment(new Gallery(), "RSVP");
        }
        viewPager.setAdapter(adapter);
    }
}
