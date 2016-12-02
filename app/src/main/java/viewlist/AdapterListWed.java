package viewlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import database.WedPojo;
import pkapoor.wed.R;

public class AdapterListWed extends RecyclerView.Adapter<AdapterListWed.MyViewHolder> {

    private ArrayList<WedPojo> arrayList;
    private static MyClickListener myClickListener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        public TextView tvNames;
        public TextView tvDate;
        public TextView tvId;
        public TextView tvType;

        public MyViewHolder(View view) {
            super(view);
            this.tvNames = (TextView) view.findViewById(R.id.tvNames);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.tvId = (TextView) view.findViewById(R.id.tvId);
            this.tvType = (TextView) view.findViewById(R.id.tvType);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getPosition(), view);
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(AdapterListWed.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public AdapterListWed(ArrayList<WedPojo> arrayList1, Context context) {
        this.arrayList = arrayList1;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNames.setText(arrayList.get(position).getName());
        holder.tvDate.setText(arrayList.get(position).getDate());
        holder.tvId.setText(arrayList.get(position).getId());
        holder.tvType.setText(arrayList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}