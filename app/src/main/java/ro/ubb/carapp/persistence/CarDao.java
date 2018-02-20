package ro.ubb.carapp.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

import ro.ubb.carapp.model.Car;


@Dao
public interface CarDao {

    @Query("SELECT * FROM car")
    List<Car> getAll();


    @Query("SELECT * FROM car where id = :id")
    Car getCarById(Long id);

    @Insert
    void insertAll(Car... cars);


    @Delete
    void delete(Car car);


    @Update
    void update(Car car);
}

