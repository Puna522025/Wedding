package viewlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseHandler;
import database.WedPojo;
import launchDetails.Config;
import pkapoor.wed.R;

/**
 * Created by pkapo8 on 12/2/2016.
 */

public class ShowList extends AppCompatActivity {

    DatabaseHandler database;

    RecyclerView listCreatedWed, listViewedWed;
    ArrayList<WedPojo> wedPojosCreatedWed, wedPojosViewedWed;
    AdapterListWed adapterListWed,adapterListViewed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_viewed_list);
        database = new DatabaseHandler(this);

        listCreatedWed = (RecyclerView) findViewById(R.id.listCreatedWed);
        listViewedWed = (RecyclerView) findViewById(R.id.listViewedWed);

        wedPojosCreatedWed = new ArrayList<>();
        wedPojosViewedWed = new ArrayList<>();

        List<WedPojo> wedPojos = database.getAllWedDetails();
        for (int i = 0; i < wedPojos.size(); i++) {
            if (wedPojos.get(i).getType().equalsIgnoreCase(Config.TYPE_WED_CREATED)) {
                wedPojosCreatedWed.add(wedPojos.get(i));
            } else if (wedPojos.get(i).getType().equalsIgnoreCase(Config.TYPE_WED_VIEWED)) {
                wedPojosViewedWed.add(wedPojos.get(i));
            }
        }

        setListLayout(listCreatedWed);
        setListLayout(listViewedWed);

        adapterListWed = new AdapterListWed(wedPojosCreatedWed, this);
        listCreatedWed.setAdapter(adapterListWed);

        adapterListViewed = new AdapterListWed(wedPojosViewedWed, this);
        listViewedWed.setAdapter(adapterListViewed);

        ((AdapterListWed) adapterListWed).setOnItemClickListener(new AdapterListWed.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                TextView tvId = (TextView) v.findViewById(R.id.tvId);
                Toast.makeText(ShowList.this, tvId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setListLayout(RecyclerView list) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
    }
}
