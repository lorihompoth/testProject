package ro.ubb.carapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.model.OwnedCar;
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;

public class ReleaseCarActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    public EditText releaseId;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_car);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        releaseId = (EditText) findViewById(R.id.releaseId);
//        progressBar = findViewById(R.id.progressBarRelease);
//        progressBar.setMax(100);
//        progressBar.setVisibility(View.GONE);

    }

    public void releaseClick(View view) {
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setProgress(25);
        final OwnedCar car = new OwnedCar();
        car.setId(Integer.parseInt(releaseId.getText().toString()));
        final OwnedCar carOwned = Globals.ownedCarRepository.getOwnedCarById(car.getId());
//        progressBar.incrementProgressBy(25);
        if(carOwned.getDays() < 30) {
            apiInterface.returnCar(car).enqueue(new Callback<Car>() {
                @Override
                public void onResponse(Call<Car> call, Response<Car> response) {
//                progressBar.incrementProgressBy(25);
                    if (response.code() != 404) {
                        Globals.ownedCarRepository.delete(car);
                        Toast.makeText(ReleaseCarActivity.this, "Returned car: " + response.body().getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReleaseCarActivity.this, "404 code received", Toast.LENGTH_SHORT).show();
                    }
//                progressBar.incrementProgressBy(25);
                }

                @Override
                public void onFailure(Call<Car> call, Throwable t) {
//                progressBar.incrementProgressBy(25);
                    Toast.makeText(ReleaseCarActivity.this, "Connection seems down", Toast.LENGTH_SHORT).show();
//                progressBar.incrementProgressBy(25);
                }
            });
        }
            finish();
    }
}
