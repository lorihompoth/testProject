package ro.ubb.carapp.utils;


import java.util.ArrayList;
import java.util.List;

import ro.ubb.carapp.model.Car;
import ro.ubb.carapp.model.OwnedCar;
import ro.ubb.carapp.persistence.CarRepository;
import ro.ubb.carapp.persistence.OwnedCarRepository;
import ro.ubb.carapp.view.CarAdapter;
import ro.ubb.carapp.view.OwnedCarAdapter;


public class Globals {

    public static CarAdapter carAdapter;

    public static OwnedCarAdapter ownedCarAdapter;

    public static List<Car> cars = new ArrayList<>();

    public static List<OwnedCar> boughtCars = new ArrayList<>();

    public static OwnedCarRepository ownedCarRepository;

    public static CarRepository carRepository;

}
