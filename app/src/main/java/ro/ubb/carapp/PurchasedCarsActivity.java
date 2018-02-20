package ro.ubb.carapp;

import android.content.Intent;
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
import ro.ubb.carapp.model.OwnedCar;
import ro.ubb.carapp.persistence.AppDatabase;
import ro.ubb.carapp.persistence.CarRepository;
import ro.ubb.carapp.persistence.OwnedCarRepository;
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;
import ro.ubb.carapp.view.CarAdapter;
import ro.ubb.carapp.view.OwnedCarAdapter;
import ro.ubb.carapp.websocket.ServerConnection;

public class PurchasedCarsActivity extends AppCompatActivity {

    private static final String TAG = "ClientLOG";
    private static final String PENDING_TEXT = "Pending: ";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private APIInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_cars);

        final Button releaseButtonClient = findViewById(R.id.releaseButtonClient);

        if(Globals.ownedCarRepository == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Thread that handles car repo started!");
                    Globals.ownedCarRepository = new OwnedCarRepository(AppDatabase.getAppDatabase(getApplicationContext()));
                    Globals.boughtCars = Globals.ownedCarRepository.getAll();
                }
            }).start();
        }

        releaseButtonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchasedCarsActivity.this, ReleaseCarActivity.class);
                startActivity(intent);
            }
        });
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_owned);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OwnedCarAdapter(new ArrayList<OwnedCar>(), PurchasedCarsActivity.this);
        Globals.ownedCarAdapter = (OwnedCarAdapter) mAdapter;
        mRecyclerView.setAdapter(mAdapter);
//        if(isNetworkAvailable()) {
//            Log.d(TAG, "Trying to get all the cars!");
//            apiInterface.getCars().enqueue(new Callback<List<Car>>() {
//                @Override
//                public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
//                    Globals.cars = response.body();
//
//                    Log.d(TAG, "Success! got all the cars !");
//                }
//
//                @Override
//                public void onFailure(Call<List<Car>> call, Throwable t) {
//
//                    Toast.makeText(ClientActivity.this, "Failed getting all the cars!" + t.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "Fail!"+t.getMessage());
//                }
//            });
//        }
//        else {
//        }
 Globals.ownedCarAdapter.setDataset(Globals.ownedCarRepository.getAll());
        Globals.ownedCarAdapter.notifyDataSetChanged();
    }
}
