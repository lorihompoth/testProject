package ro.ubb.carapp.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

import ro.ubb.carapp.model.OwnedCar;

@Dao
public interface OwnedCarDao {

    @Query("SELECT * FROM ownedcar")
    List<OwnedCar> getAll();


    @Query("SELECT * FROM ownedcar where id = :id")
    OwnedCar getOwnedCarById(Long id);

    @Insert
    void insertAll(OwnedCar... ownedcars);


    @Delete
    void delete(OwnedCar ownedcar);


    @Update
    void update(OwnedCar ownedcar);
}

