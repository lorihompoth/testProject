package ro.ubb.carapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import ro.ubb.carapp.R;
import ro.ubb.carapp.model.Car;



public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private List<Car> cars;
    private Context context;
    // Provide a reference to the views for each data car
    // Complex data cars may need more than one view per car, and
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
    public CarAdapter(List<Car> myDataset, Context context) {
        cars = myDataset;
        this.context = context;
    }

    public void setDataset(List<Car> dataset) {
        this.cars = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        holder.mTextView.setText(""+cars.get(position).getId()+". "+ cars.get(position).getName()+ " | "+cars.get(position).getType()+" | "+cars.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public List<Car> getDataset() {
        return cars;
    }
}
