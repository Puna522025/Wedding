package galleryList;

/**
 * Created by pkapo8 on 11/23/2016.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pkapoor.wed.R;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<Integer> arrayList;
    private static MyClickListener myClickListener;
    Context context;
    String type;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.imagebr);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getPosition(), view, type);
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v, String type);
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public Adapter(ArrayList<Integer> arrayList1, Context context, String type) {
        this.arrayList = arrayList1;
        this.context = context;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.image.setImageDrawable(ContextCompat.getDrawable(context, arrayList.get(position)));
        Picasso.with(context).load(arrayList.get(position)).resize(120, 80).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}