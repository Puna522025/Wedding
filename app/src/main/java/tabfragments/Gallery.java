package tabfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import galleryList.Adapter;
import galleryList.ViewFullScreenImage;
import launchDetails.Config;
import pkapoor.wed.BlurBuilder;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 11/23/2016.
 */

public class Gallery extends Fragment {


    ArrayList<Integer> bridImages, gxxxmImages, rxxxaImages;
    Adapter adapter;
    RecyclerView listBxxxx, listRxxx, listGrxx;
    TextView tvEventrok, tvEventBri, tvEventGGGG, tvEventRSV, tvRsvpText, tvrsvpFaml1, tvRsvpFamlContact1, tvRsvpFaml2, tvRsvpFamlContact2;

    SharedPreferences sharedPreferences;
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

        bridImages = new ArrayList<>();
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
        });

        tvEventrok = (TextView) view.findViewById(R.id.Event);
        tvEventBri = (TextView) view.findViewById(R.id.EventBri);
        tvEventGGGG = (TextView) view.findViewById(R.id.EventGGGG);

        tvEventRSV = (TextView) view.findViewById(R.id.EventRSV);
        tvRsvpText = (TextView) view.findViewById(R.id.rsvrText);
        tvrsvpFaml1 = (TextView) view.findViewById(R.id.rsvrFaml1);
        tvRsvpFaml2 = (TextView) view.findViewById(R.id.rsvrFaml2);
        tvRsvpFamlContact1 = (TextView) view.findViewById(R.id.rsvrFamlContact1);
        tvRsvpFamlContact2 = (TextView) view.findViewById(R.id.rsvrFamlContact2);


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

        setRSVPdetails();
        return view;
    }

    private void setRSVPdetails() {
        //tvRsvpText.setText(sharedPreferences.getString("we would like to jsdjkbf", "DATE"));
        tvRsvpText.setText("vg.sdkmvk;sdmv;lsdmv;sdm,vsdvl;vm;sdlvmsdl;mvl;sd");
        tvrsvpFaml1.setText(sharedPreferences.getString(Config.rsvp_name1, "name1"));
        tvRsvpFaml2.setText(sharedPreferences.getString(Config.rsvp_name2, "name2"));
        tvRsvpFamlContact1.setText(sharedPreferences.getString(Config.rsvp_phone_one, "phone1"));
        tvRsvpFamlContact2.setText(sharedPreferences.getString(Config.rsvp_phone_two, "phone2"));
    }

    private void setListLayout(RecyclerView list) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //LinearLayoutManager llm = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}

