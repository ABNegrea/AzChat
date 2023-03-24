package ReteaDeSocializare.Domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Friend extends Entity<UUID>{
    private String name;

    private UUID id;
    private String email;
    private String friendsFrom;

    public Friend(String name, String email, String friendsFrom) {
        this.name = name;
        this.email = email;
        this.friendsFrom = friendsFrom;
    }

    public Friend(UUID id, String name, String email, String friendsFrom) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.friendsFrom = friendsFrom;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(String friendsFrom) {
        this.friendsFrom = friendsFrom;
    }
}
