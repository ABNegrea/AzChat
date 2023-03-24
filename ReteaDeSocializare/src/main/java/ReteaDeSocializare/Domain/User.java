package ReteaDeSocializare.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID>
{
    private String firstName;
    private String lastName;
    private List<User> friends;

    private String password;

    private String email;

    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.setId(UUID.randomUUID());
        friends = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(UUID id, String firstName, String lastName, String password, String email)
    {
        this.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        friends = new ArrayList<>();
    }

    public User( String firstName, String lastName, String password, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        friends = new ArrayList<>();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User(UUID id, String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.setId(id);
        friends = new ArrayList<>();
    }
    public String getFirstName()
    {
        return firstName;
    }

    public void setfirstName(String firstName)
    {
        firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setlastName(String lastName)
    {
        lastName = lastName;
    }

    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    public List<User> getFriends()
    {
        return friends;
    }

    public void setFriends(List<User> friends2)
    {
        friends = friends2;
    }

    @Override
    public String toString()
    {
        return "Name: " + firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, friends);
    }
}