package ro.ubb.carapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.retrofit.APIClient;
import ro.ubb.carapp.retrofit.APIInterface;
import ro.ubb.carapp.utils.Globals;

public class AddCarActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    public EditText addName;
    public EditText addQuantity;
    public EditText addType;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        addName = (EditText) findViewById(R.id.addName);
        addQuantity = (EditText) findViewById(R.id.addQuantity);
        addType = (EditText) findViewById(R.id.addType);
        progressBar = findViewById(R.id.progressBarAdd);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);

    }

    public void addClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(25);
        Car car = new Car();
        car.setType(addType.getText().toString());
        car.setName(addName.getText().toString());
        car.setQuantity(Integer.parseInt(addQuantity.getText().toString()));
        progressBar.incrementProgressBy(25);
        apiInterface.addCar(car).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                progressBar.incrementProgressBy(25);
                if(response.code()!=404) {
//                    Globals.tables.add(response.body());
                    Toast.makeText(AddCarActivity.this, "Added car: "+response.body().getName(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddCarActivity.this, "404 code received", Toast.LENGTH_SHORT).show();
                }
                progressBar.incrementProgressBy(25);
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                progressBar.incrementProgressBy(25);
                Toast.makeText(AddCarActivity.this, "Connection seems down", Toast.LENGTH_SHORT).show();
                progressBar.incrementProgressBy(25);
            }
        });
        finish();
    }
}
