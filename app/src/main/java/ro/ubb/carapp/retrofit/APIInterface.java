package ro.ubb.carapp.retrofit;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.model.OwnedCar;


public interface APIInterface {

    @GET("/all")
    Call<List<Car>> getCars();
//
    @POST("/addCar")
    Call<Car> addCar(@Body Car car);

    @POST("/removeCar")
    Call<Car> deleteCar(@Body Car car);

    @POST("/buyCar")
    Call<OwnedCar> buyCar(@Body OwnedCar car);

    @POST("/returnCar")
    Call<Car> returnCar(@Body Car car);

}
