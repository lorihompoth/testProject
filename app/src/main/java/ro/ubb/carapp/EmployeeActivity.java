package ro.ubb.carapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;
import ro.ubb.carapp.view.CarAdapter;
import ro.ubb.carapp.websocket.ServerConnection;

public class EmployeeActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeLOG";
    private static final String PENDING_TEXT = "Pending: ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private APIInterface apiInterface;
    private ServerConnection serverConnection;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        final Button addButton = findViewById(R.id.addButtonEmployee);

        final Button deleteButton = findViewById(R.id.deleteButtonEmployee);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, AddCarActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);
        gson = new Gson();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CarAdapter(new ArrayList<Car>(), EmployeeActivity.this);
        Globals.carAdapter = (CarAdapter) mAdapter;
        mRecyclerView.setAdapter(mAdapter);
        if(isNetworkAvailable()) {
            Log.d(TAG, "Trying to get all the cars!");
            apiInterface.getCars().enqueue(new Callback<List<Car>>() {
                @Override
                public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                    Globals.cars = response.body();
                    Globals.carAdapter.setDataset(Globals.cars);
                    Globals.carAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Success! got all the cars !");
                }

                @Override
                public void onFailure(Call<List<Car>> call, Throwable t) {

                    Toast.makeText(EmployeeActivity.this, "Failed getting all the cars!" + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Fail!"+t.getMessage());
                }
            });
        }
        else {
            Globals.carAdapter.setDataset(Globals.carRepository.getAll());
            Globals.carAdapter.notifyDataSetChanged();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
