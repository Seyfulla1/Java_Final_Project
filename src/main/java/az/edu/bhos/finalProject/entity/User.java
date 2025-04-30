package az.edu.bhos.finalProject.entity;

public class User extends Passenger{
    private String username;
    private String password;

    public User(String name, String surname, String username, String password) {
        super(name, surname);
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
    @Override
    public boolean equals(Object that){
        if(this==that){
            return true;
        }
        if(!(that instanceof User)){
            return false;
        }
        User thatUser = (User) that;
        return super.equals(thatUser)
                && this.username.equals(thatUser.getUsername());
    }
    @Override
    public int hashCode() {
        return super.hashCode() + username.hashCode();
    }
}
