package ro.ubb.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.model.OwnedCar;
import ro.ubb.carapp.persistence.AppDatabase;
import ro.ubb.carapp.persistence.OwnedCarRepository;
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;

public class BuyCarActivity extends AppCompatActivity {

    private APIInterface apiInterface;
    public EditText buyId;
    public EditText buyQuantity;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_car);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        buyId = (EditText) findViewById(R.id.buyId);
        buyQuantity = (EditText) findViewById(R.id.buyQuantity);
        progressBar = findViewById(R.id.progressBarBuy);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);

        if(Globals.ownedCarRepository == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Globals.ownedCarRepository = new OwnedCarRepository(AppDatabase.getAppDatabase(getApplicationContext()));
                    Globals.boughtCars = Globals.ownedCarRepository.getAll();
                }
            }).start();
        }

    }

    public void buyClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(25);
        final OwnedCar car = new OwnedCar();
        car.setId(Integer.parseInt(buyId.getText().toString()));
        car.setQuantity(Integer.parseInt(buyQuantity.getText().toString()));
        progressBar.incrementProgressBy(25);
        apiInterface.buyCar(car).enqueue(new Callback<OwnedCar>() {
            @Override
            public void onResponse(Call<OwnedCar> call, Response<OwnedCar> response) {
                progressBar.incrementProgressBy(25);
                if(response.code()!=404) {
                    car.setId(response.body().getId());
                    car.setStatus(response.body().getStatus());
                    car.setType(response.body().getType());
                    Globals.ownedCarRepository.add(car);
//                    Globals.tables.add(response.body());
                    Toast.makeText(BuyCarActivity.this, "buyed car: "+response.body().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BuyCarActivity.this, PurchasedCarsActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(BuyCarActivity.this, "404 code received", Toast.LENGTH_SHORT).show();
                }
                progressBar.incrementProgressBy(25);
            }

            @Override
            public void onFailure(Call<OwnedCar> call, Throwable t) {
                progressBar.incrementProgressBy(25);
                Toast.makeText(BuyCarActivity.this, "Connection seems down", Toast.LENGTH_SHORT).show();
                progressBar.incrementProgressBy(25);
            }
        });
    }
}
