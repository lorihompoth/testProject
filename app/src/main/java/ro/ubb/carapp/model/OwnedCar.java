package ro.ubb.carapp.model;

import android.arch.persistence.room.Entity;

@Entity(tableName = "ownedcar")
public class OwnedCar extends Car {
    private int days;

    public OwnedCar(Integer id, String name, Integer quantity, String type, String status) {
        super(id, name, quantity, type, status);
        this.days = 0;
    }

    public OwnedCar(){

    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OwnedCar ownedCar = (OwnedCar) o;

        return days == ownedCar.days;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + days;
        return result;
    }

    @Override
    public String toString() {
        return "OwnedCar{" +
                "days=" + days +
                '}';
    }
}
