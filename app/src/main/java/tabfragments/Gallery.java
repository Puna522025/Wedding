package tabfragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import galleryList.Adapter;
import launchDetails.Config;
import pkapoor.wed.BlurBuilder;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class Gallery extends Fragment implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST = 11;
    ArrayList<Integer> bridImages, gxxxmImages, rxxxaImages;
    Adapter adapter;
    RecyclerView listBxxxx, listRxxx, listGrxx;
    TextView tvEventrok, tvEventBri, tvEventGGGG, tvEventRSV, tvRsvpText, tvrsvpFaml1, tvRsvpFamlContact1, tvRsvpFaml2, tvRsvpFamlContact2;
    CardView card4;
    SharedPreferences sharedPreferences;
    RelativeLayout rlContactOne, rlContactTwo, rlRSV;
    public static final String MyPREFERENCES = "myPreference";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gallery, container, false);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);

        Bitmap bb = BlurBuilder.blur(getContext(), bitmap);
        Drawable d = new BitmapDrawable(getResources(), bb);
        view.setBackground(d);

        /*bridImages = new ArrayList<>();
        bridImages.add(0, R.drawable.aa);
        bridImages.add(1, R.drawable.ss);
        bridImages.add(2, R.drawable.aa);
        bridImages.add(3, R.drawable.ss);
        bridImages.add(4, R.drawable.aa);
        bridImages.add(5, R.drawable.ss);
        bridImages.add(6, R.drawable.aa);
        bridImages.add(7, R.drawable.ss);
        bridImages.add(8, R.drawable.aa);
        bridImages.add(9, R.drawable.ss);
        bridImages.add(10, R.drawable.ss);
        bridImages.add(11, R.drawable.aa);
        bridImages.add(12, R.drawable.ss);

        rxxxaImages = new ArrayList<>();
        rxxxaImages.add(0, R.drawable.aa);
        rxxxaImages.add(1, R.drawable.ss);
        rxxxaImages.add(2, R.drawable.aa);


        gxxxmImages = new ArrayList<>();
        gxxxmImages.add(0, R.drawable.aa);
        gxxxmImages.add(1, R.drawable.ss);


        listBxxxx = (RecyclerView) view.findViewById(R.id.listBrid);
        listRxxx = (RecyclerView) view.findViewById(R.id.listRxxx);
        listGrxx = (RecyclerView) view.findViewById(R.id.listGrxx);

        setListLayout(listBxxxx);
        setListLayout(listRxxx);
        setListLayout(listGrxx);

        adapter = new Adapter(bridImages, getContext(), "brxxx");
        listBxxxx.setAdapter(adapter);

        adapter = new Adapter(rxxxaImages, getContext(), "Rxxxx");
        listRxxx.setAdapter(adapter);

        adapter = new Adapter(gxxxmImages, getContext(), "Grxxx");
        listGrxx.setAdapter(adapter);

        ((Adapter) adapter).setOnItemClickListener(new Adapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v, String type) {

                Intent intent = new Intent(getContext(), ViewFullScreenImage.class);
                intent.putExtra("position", position);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });*/

        tvEventrok = (TextView) view.findViewById(R.id.Event);
        tvEventBri = (TextView) view.findViewById(R.id.EventBri);
        tvEventGGGG = (TextView) view.findViewById(R.id.EventGGGG);

        tvEventRSV = (TextView) view.findViewById(R.id.EventRSV);
        tvRsvpText = (TextView) view.findViewById(R.id.rsvrText);
        tvrsvpFaml1 = (TextView) view.findViewById(R.id.rsvrFaml1);
        tvRsvpFaml2 = (TextView) view.findViewById(R.id.rsvrFaml2);
        tvRsvpFamlContact1 = (TextView) view.findViewById(R.id.rsvrFamlContact1);
        tvRsvpFamlContact2 = (TextView) view.findViewById(R.id.rsvrFamlContact2);

        rlContactOne = (RelativeLayout) view.findViewById(R.id.rlContactOne);
        rlContactTwo = (RelativeLayout) view.findViewById(R.id.rlContactTwo);
        rlRSV = (RelativeLayout) view.findViewById(R.id.rlRSV);

        card4 = (CardView) view.findViewById(R.id.card4);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DeliusSwashCaps-Regular.ttf");

        tvEventrok.setTypeface(type);
        tvEventBri.setTypeface(type);
        tvEventGGGG.setTypeface(type);

        tvEventRSV.setTypeface(type);
        tvRsvpText.setTypeface(type);
        tvrsvpFaml1.setTypeface(type);
        tvRsvpFaml2.setTypeface(type);
        tvRsvpFamlContact1.setTypeface(type);
        tvRsvpFamlContact2.setTypeface(type);
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        rlContactOne.setOnClickListener(this);
        rlContactTwo.setOnClickListener(this);

        changeTextColor();

        setRSVPdetails();
        return view;
    }

    private void changeTextColor() {
        int colorSelected = sharedPreferences.getInt(Config.colorSelected, R.color.colorRed);

        switch (colorSelected) {
            case R.color.colorRed:
                changeColor(colorSelected);
                break;
            case R.color.PinkKittyToolBar:
                changeColor(colorSelected);
                break;
            case R.color.GreenToolBar:
                changeColor(colorSelected);
                break;
            case R.color.BlackToolBar:
                changeColor(colorSelected);
                break;
            case R.color.BlueToolBar:
                changeColor(colorSelected);
                break;
        }
    }

    private void changeColor(int colorSelected) {
        tvrsvpFaml1.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        tvRsvpFaml2.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        tvRsvpFamlContact1.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        tvRsvpFamlContact2.setTextColor(ContextCompat.getColor(getContext(), colorSelected));
        rlRSV.setBackgroundColor(ContextCompat.getColor(getContext(), colorSelected));
    }

    private void setRSVPdetails() {

        if (sharedPreferences.getString(Config.rsvp_tobe, "true").equalsIgnoreCase("true")) {
            card4.setVisibility(View.VISIBLE);

            tvRsvpText.setText(sharedPreferences.getString(Config.rsvp_text, "name1"));
            tvrsvpFaml1.setText(sharedPreferences.getString(Config.rsvp_name1, "name1"));
            tvRsvpFaml2.setText(sharedPreferences.getString(Config.rsvp_name2, "name2"));

            String phoneOne = sharedPreferences.getString(Config.rsvp_phone_one, "");
            String phoneTwo = sharedPreferences.getString(Config.rsvp_phone_two, "");

            if (!TextUtils.isEmpty(phoneOne)) {
                tvRsvpFamlContact1.setText(phoneOne);
                rlContactOne.setVisibility(View.VISIBLE);
            } else {
                rlContactOne.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(phoneTwo)) {
                tvRsvpFamlContact2.setText(phoneTwo);
                rlContactTwo.setVisibility(View.VISIBLE);
            } else {
                rlContactTwo.setVisibility(View.GONE);
            }
        } else {
            card4.setVisibility(View.GONE);
        }
    }

    private void setListLayout(RecyclerView list) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlContactOne:
                String phoneOne = sharedPreferences.getString(Config.rsvp_phone_one, "");
                if (!TextUtils.isEmpty(phoneOne)) {
                    makeCall(phoneOne);
                }
                break;
            case R.id.rlContactTwo:
                String phoneTwo = sharedPreferences.getString(Config.rsvp_phone_two, "");
                if (!TextUtils.isEmpty(phoneTwo)) {
                    makeCall(phoneTwo);
                }
                break;
        }
    }

    private void makeCall(String phoneNumber) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST);
            } else {
                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "No call support", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted!!

                } else {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CALL_PHONE)) {
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
                return;
            }
        }
    }

}

