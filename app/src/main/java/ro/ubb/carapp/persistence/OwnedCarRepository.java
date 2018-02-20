package ro.ubb.carapp.persistence;


import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ro.ubb.carapp.model.OwnedCar;


public class OwnedCarRepository {

    private static final String TAG = "OwnedCarRepositoryLOG";
    private List<OwnedCar> repo;
    private final AppDatabase appDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public OwnedCarRepository(final AppDatabase appDatabase) {
        this.repo = new ArrayList<>();
        this.appDatabase = appDatabase;
        executor.execute(new Runnable() {

            @Override
            public void run() {
                repo.addAll(appDatabase.ownedCarDao().getAll());
            }
        });
    }


    public void add(final OwnedCar e) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                repo.add(e);
                appDatabase.ownedCarDao().insertAll(e);
            }
        });
    }

    public void update(final OwnedCar e) {
        OwnedCar initialOwnedCar = null;
        for(OwnedCar sl : repo) {
            if(sl.getId() == e.getId()) {
                initialOwnedCar = sl;
                break;
            }
        }
        initialOwnedCar.setName(e.getName());
        initialOwnedCar.setStatus(e.getStatus());
        initialOwnedCar.setType(e.getType());
        final OwnedCar toUpdateList = initialOwnedCar;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.ownedCarDao().update(toUpdateList);
            }
        });

    }

    public void replace(final List<OwnedCar> newRepo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<OwnedCar> toDelete = new ArrayList<>();
                for(OwnedCar b:repo) {
                    toDelete.add(b);
                }
                for(OwnedCar b: toDelete) {
                    appDatabase.ownedCarDao().delete(b);

                }
                repo.clear();
            }
        });
        for(OwnedCar toAdd:newRepo) {
            add(toAdd);
        }
    }

    public void replaceFirst(final OwnedCar firstNewOwnedCar) {
        final List<OwnedCar> ownedcars = new ArrayList<>();
        for(OwnedCar b: repo) {
            ownedcars.add(b);
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {

                for(OwnedCar b: ownedcars) {
                    appDatabase.ownedCarDao().delete(b);
                }
                repo.clear();
            }
        });
        while(!repo.isEmpty());
        ownedcars.remove(0);
        ownedcars.add(0,firstNewOwnedCar);
        for(OwnedCar b:ownedcars) {
            appDatabase.ownedCarDao().insertAll(b);
        }
    }

    public void delete(final OwnedCar ownedcar) {

        for(OwnedCar sl : repo) {
            if(sl.getId() == ownedcar.getId()) {
                repo.remove(sl);
                final OwnedCar toDelete = sl;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.ownedCarDao().delete(toDelete);
                    }
                });
                break;
            }
        }
    }

    public List<OwnedCar> getAll() {
        return repo;
    }

    public OwnedCar getOwnedCarById(final Integer id) {
        for(OwnedCar p:repo) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(Objects.equals(p.getId(), id)) {
                    return p;
                }
            }
        }
        return null;
    }
}
