package ro.ubb.carapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.persistence.AppDatabase;
import ro.ubb.carapp.persistence.CarRepository;
import ro.ubb.carapp.persistence.OwnedCarRepository;
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;
import ro.ubb.carapp.websocket.ServerConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_LOG";
    private APIInterface apiInterface;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Button employeeButton = findViewById(R.id.employeeButton);
        Button clientButton = findViewById(R.id.clientButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

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

        if(Globals.carRepository == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Thread that handles car repo started!");
                    Globals.carRepository = new CarRepository(AppDatabase.getAppDatabase(getApplicationContext()));
                    Globals.cars = Globals.carRepository.getAll();
                }
            }).start();
        }
        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Opening employee activity!");
                Intent intent = new Intent(MainActivity.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Opening client activity!");

                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
