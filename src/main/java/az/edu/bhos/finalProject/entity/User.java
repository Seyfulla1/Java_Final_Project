package az.edu.bhos.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User{
    private String username;
    private String password;
    private Passenger passenger;

    @JsonCreator
    public User(@JsonProperty("passenger") Passenger passenger,@JsonProperty("username") String username,@JsonProperty("password") String password) {
        this.passenger = passenger;
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Passenger getPassenger() {
        return passenger;
    }
    @Override
    public boolean equals(Object that){
        if(this==that){
            return true;
        }
        if(!(that instanceof User)){
            return false;
        }
        User thatUser = (User) that;
        return this.passenger.equals(thatUser.passenger) && this.username.equals(thatUser.username);
    }
    @Override
    public int hashCode() {
        return super.hashCode() + username.hashCode();
    }
}
