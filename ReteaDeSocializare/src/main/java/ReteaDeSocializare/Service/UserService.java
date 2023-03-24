package ReteaDeSocializare.Service;

import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserService {
    private Repository<UUID, User> userRepo;

    public UserService(Repository<UUID, User> userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Returns all users from the repo
     * @return list of users from repo
     */
    public ArrayList<User> getUsers(){
        return new ArrayList<>( (Collection<User>) userRepo.findAll());
    }

    /**
     * Adds an user to the repository
     * @param lastName must not be null
     * @param firstName must not be null
     */
    public void addUser(String lastName, String firstName, String email, String password){
        User user = new User(UUID.randomUUID(),firstName, lastName, password, email);
        userRepo.save(user);
    }

    public void addUser(String lastName, String firstName){
        User user = new User(firstName, lastName);
        userRepo.save(user);
    }

    /**
     * Deletes an user from repo
     * @param user user must not be null
     */
    public void removeUser(User user){
        UUID id = user.getId();
        userRepo.delete(id);
    }

    /**
     * Gets the full name of an user by it's id
     * @param id the id
     * @return a string that contains the full name
     */
    public String getFullName(UUID id){
        Collection<User> users = (Collection<User>) userRepo.findAll();
        for(User u : users)
            if(u.getId().equals(id))
                return u.getFullName();
        return null;
    }

    public String getEmail(UUID id){
        Collection<User> users = (Collection<User>) userRepo.findAll();
        for(User u : users)
            if(u.getId().equals(id))
                return u.getEmail();
        return null;
    }

    public UUID getUserbyEmail(String email){
        Collection<User> users = (Collection<User>) userRepo.findAll();
        for(User u : users)
            if(u.getEmail().equals(email))
                return u.getId();
        return null;
    }

    /**
     * Returns all the users that have the first name as a given string
     * @param string first name of an user
     * @return an array with all the users
     */
    public ArrayList<User> getUsersWithFirstName(String string) {
        Collection<User> users = (Collection<User>) userRepo.findAll();
        return new ArrayList<>(users.stream().filter((User x) -> {return x.getFirstName().equals(string);}).toList());
    }

    /**
     * Removes an user from all other user's friend lists
     * @param user user must not be null
     * removes user from the friendlist of the other users
     */
    public void removeUserfromUsers(User user){
        List<User> userFriends;
        for(User u : getUsers()){
            userFriends = u.getFriends();
            userFriends.remove(user);
        }
    }

    /**
     * Adds a friend to an user's friend list and vice-versa
     * @param id1 first user
     * @param id2 second user
     */
    public void addFriend(UUID id1, UUID id2){
        userRepo.findOne(id1).getFriends().add(userRepo.findOne(id2));
        userRepo.findOne(id2).getFriends().add(userRepo.findOne(id1));
    }

    public User getUserFromId(UUID id){

        return userRepo.findOne(id);
    }
}
