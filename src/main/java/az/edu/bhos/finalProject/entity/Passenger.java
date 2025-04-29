package az.edu.bhos.finalProject.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Passenger {
    private final String name;
    private final String surname;

    @JsonCreator
    public Passenger(@JsonProperty("name") String name, @JsonProperty("surname") String surname) {
        this.name = name;
        this.surname = surname;

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof Passenger)) {
            return false;
        }
        Passenger thatP = (Passenger) that;
        return this.name.equals(thatP.getName()) && this.surname.equals(thatP.getSurname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }
}

