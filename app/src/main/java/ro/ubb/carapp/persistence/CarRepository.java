package ro.ubb.carapp.persistence;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ro.ubb.carapp.model.Car;



public class CarRepository {

    private static final String TAG = "CarRepositoryLOG";
    private List<Car> repo;
    private final AppDatabase appDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CarRepository(final AppDatabase appDatabase) {
        this.repo = new ArrayList<>();
        this.appDatabase = appDatabase;
        executor.execute(new Runnable() {

            @Override
            public void run() {
                repo.addAll(appDatabase.carDao().getAll());
            }
        });
    }


    public void add(final Car e) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repo.add(e);
                appDatabase.carDao().insertAll(e);
            }
        });
    }

    public void update(final Car e) {
        Car initialCar = null;
        for(Car sl : repo) {
            if(sl.getId() == e.getId()) {
                initialCar = sl;
                break;
            }
        }
        initialCar.setName(e.getName());
        initialCar.setStatus(e.getStatus());
        initialCar.setType(e.getType());
        final Car toUpdateList = initialCar;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.carDao().update(toUpdateList);
            }
        });

    }

    public void replace(final List<Car> newRepo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Car> toDelete = new ArrayList<>();
                for(Car b:repo) {
                    toDelete.add(b);
                }
                for(Car b: toDelete) {
                    appDatabase.carDao().delete(b);

                }
                repo.clear();
            }
        });
        for(Car toAdd:newRepo) {
            add(toAdd);
        }
    }

    public void replaceFirst(final Car firstNewCar) {
        final List<Car> cars = new ArrayList<>();
        for(Car b: repo) {
            cars.add(b);
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {

                for(Car b: cars) {
                    appDatabase.carDao().delete(b);
                }
                repo.clear();
            }
        });
        while(!repo.isEmpty());
        cars.remove(0);
        cars.add(0,firstNewCar);
        for(Car b:cars) {
            appDatabase.carDao().insertAll(b);
        }
    }

    public void delete(final Car car) {

        for(Car sl : repo) {
            if(sl.getId() == car.getId()) {
                repo.remove(sl);
                final Car toDelete = sl;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.carDao().delete(toDelete);
                    }
                });
                break;
            }
        }
    }

    public List<Car> getAll() {
        return repo;
    }

    public Car getCarById(final Long id) {
        for(Car p:repo) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}


