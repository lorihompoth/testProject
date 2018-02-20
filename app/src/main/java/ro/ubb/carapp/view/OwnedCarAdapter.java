package ro.ubb.carapp.view;

/**
 * Created by CristianCosmin on 25.01.2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ro.ubb.carapp.R;
import ro.ubb.carapp.model.OwnedCar;


public class OwnedCarAdapter extends RecyclerView.Adapter<OwnedCarAdapter.ViewHolder> {
    private List<OwnedCar> ownedCars;
    private Context context;
    // Provide a reference to the views for each data car
    // Complex data ownedCars may need more than one view per car, and
    // you provide access to all the views for a data car in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data car is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OwnedCarAdapter(List<OwnedCar> myDataset, Context context) {
        ownedCars = myDataset;
        this.context = context;
    }

    public void setDataset(List<OwnedCar> dataset) {
        this.ownedCars = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OwnedCarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt(((TextView)v).getText().toString().split("-")[0]);
//                Intent intent = new Intent(context, GetActivity.class);
//                Bundle b = new Bundle();
//                b.putInt("position", position);
//                intent.putExtras(b);
//                context.startActivity(intent);
//            }
//        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(""+ ownedCars.get(position).getId()+". "+ ownedCars.get(position).getName()+ " | "+ ownedCars.get(position).getType()+" | "+ ownedCars.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return ownedCars.size();
    }

    public List<OwnedCar> getDataset() {
        return ownedCars;
    }
}

