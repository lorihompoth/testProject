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
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;

public class DeleteActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    public EditText deleteId;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        deleteId = (EditText) findViewById(R.id.deleteId);
        progressBar = findViewById(R.id.progressBarDelete);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);

    }

    public void deleteClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(25);
        final Car car = new Car();
        car.setId(Integer.parseInt(deleteId.getText().toString()));
        progressBar.incrementProgressBy(25);
        apiInterface.deleteCar(car).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                progressBar.incrementProgressBy(25);
                if(response.code()!=404) {
                    Globals.carRepository.delete(car);
                    Toast.makeText(DeleteActivity.this, "Removed car: "+response.body().getName(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DeleteActivity.this, "404 code received", Toast.LENGTH_SHORT).show();
                }
                progressBar.incrementProgressBy(25);
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                progressBar.incrementProgressBy(25);
                Toast.makeText(DeleteActivity.this, "Connection seems down", Toast.LENGTH_SHORT).show();
                progressBar.incrementProgressBy(25);
            }
        });
        finish();
    }
}
