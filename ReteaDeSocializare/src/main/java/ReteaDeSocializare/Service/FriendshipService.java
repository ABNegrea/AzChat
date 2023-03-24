package ReteaDeSocializare.Service;

import ReteaDeSocializare.Domain.Exceptions.FriendshipException;
import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class FriendshipService {
    private Repository<UUID, Friendship> friendRepo;

    /**
     * constructor
     * @param friendRepo the repository for the service
     */
    public FriendshipService(Repository<UUID, Friendship> friendRepo) {
        this.friendRepo = friendRepo;
    }

    /**
     * Returns all the friendships stored
     * @return list of friendships
     */
    public ArrayList<Friendship> getAllFriendships() {
        return new ArrayList<>((Collection<Friendship>) friendRepo.findAll());
    }

    /**
     * Returns a friendship by a given id
     * @param id id must not be null
     * @return returns the friendship with that id
     */
    public Friendship getFriendshipById(UUID id) {

        for (Friendship f : getAllFriendships())
            if (f.getId() == id)
                return f;
        return null;
    }

    public Friendship getFriendshipByUserId(UUID id) {

        for (Friendship f : getAllFriendships())
            if (f.getId1().equals(id) || f.getId2().equals(id))
                return f;
        return null;
    }

    public Friendship findFriendship(UUID id1, UUID id2) {
        for (Friendship f : getAllFriendships())
            if (f.getId1().equals(id1) && f.getId2().equals(id2))
                return f;
            else if (f.getId1().equals(id2) && f.getId2().equals(id1))
                return f;
        return null;
    }

    /**
     * Adds a friendship made by two distinct ids
     * @param id1 first id
     * @param id2 second id
     */
    public void addFriendship(UUID id1, UUID id2) {

        Friendship newFriendship = new Friendship(id1, id2);
        Collection<Friendship> friendships = (Collection<Friendship>) friendRepo.findAll();
        for (Friendship s : friendships) {
            if (s.getId1() == newFriendship.getId1() && s.getId2() == newFriendship.getId2())
                throw new FriendshipException("These users are already friends!\n");
            if (s.getId2() == newFriendship.getId1() && s.getId1() == newFriendship.getId2())
                throw new FriendshipException("These users are already friends!\n");
        }
        friendRepo.save(newFriendship);
    }

    public void addFriendship(Friendship friendship) {
        for (Friendship s : friendRepo.findAll()) {
            if (s.getId1() == friendship.getId1() && s.getId2() == friendship.getId2())
                throw new FriendshipException("These users are already friends!\n");
            if (s.getId2() == friendship.getId1() && s.getId1() == friendship.getId2())
                throw new FriendshipException("These users are already friends!\n");
        }
        friendRepo.save(friendship);
    }

    public void changeStatusFriendship(UUID id1, UUID id2) {
        for (Friendship s : friendRepo.findAll()) {
            if((s.getId2().equals(id1) && s.getId1().equals(id2)) || (s.getId1().equals(id2) && s.getId2().equals(id1)))
                //s.setStatus("Accepted");
                friendRepo.update(s,s);
        }
    }

    /**
     * Deletes a friendship of 2 users by their ids
     * @throws  if the friendship doesn't exist
     */
    public boolean deleteFriendship(UUID id1, UUID id2) {
        boolean OK = false;
        Collection<Friendship> friendships = (Collection<Friendship>) friendRepo.findAll();
        for (Friendship s : friendships)
            if ((s.getId1().equals(id1) && s.getId2().equals(id2)) || (s.getId2().equals(id1) && s.getId1().equals(id2)))
        {
            friendRepo.delete(s.getId());
            OK = true;
        }
        if (!OK)
            throw new FriendshipException("The friendship doesn't exist!\n");
    return OK;
    }

    /**
     * Verifies if there is a friendship between 2 users
     * @param id1 first user
     * @param id2 second user
     * @return true if there is a friendship, false otherwise
     */
    public boolean checkFriendship(UUID id1, UUID id2){
        Collection<Friendship> friendships = (Collection<Friendship>) friendRepo.findAll();
        for (Friendship s : friendships)
            if ((s.getId1() == id1 && s.getId2() == id2) || (s.getId2() == id1 && s.getId1() == id2))
                return true;
        return false;
    }
}
