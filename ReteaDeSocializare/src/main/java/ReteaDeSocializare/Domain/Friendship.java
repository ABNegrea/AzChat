package ReteaDeSocializare.Domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class    Friendship extends Entity<UUID> {
    private UUID id1;
    private UUID id2;

    private String status;

    private UUID sentby;
    private LocalDateTime friendsFrom;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd YYYY hh:mm:ss");
    public Friendship(UUID id1, UUID id2) {
        this.setId(UUID.randomUUID());
            this.id1 = id1;
            this.id2 = id2;
        friendsFrom = LocalDateTime.now();
        this.sentby = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public UUID getSentby() {
        return sentby;
    }

    public void setSentby(UUID sentby) {
        this.sentby = sentby;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public Friendship(UUID id1, UUID id2, String status, UUID sentby) {
        this.setId(UUID.randomUUID());

        this.id1 = id1;
        this.id2 = id2;
        this.sentby = sentby;

        this.status = status;
        friendsFrom = LocalDateTime.now();
        dtf.format(friendsFrom);
    }

    public Friendship(UUID id, UUID id1, UUID id2, String date, String status, UUID sentby) {
        this.setId(id);

        this.id1 = id1;
        this.id2 = id2;
        this.sentby = sentby;

        this.status = status;
        friendsFrom = LocalDateTime.parse(date);
        dtf.format(friendsFrom);
    }

    public Friendship(UUID id, UUID id1, UUID id2, String date, String status) {
        this.setId(id);

        this.id1 = id1;
        this.id2 = id2;
        this.sentby = null;

        this.status = status;
        friendsFrom = LocalDateTime.parse(date);
        dtf.format(friendsFrom);
    }
    public Friendship(UUID id, UUID id1, UUID id2, LocalDateTime date) {
        this.setId(id);
            this.id1 = id1;
            this.id2 = id2;
        friendsFrom = date;
        this.sentby = null;
    }

    public UUID getId1() {
        return id1;
    }

    public void setId1(UUID id1) {
        this.id1 = id1;
    }

    public UUID getId2() {
        return id2;
    }

    public void setId2(UUID id2) {
        this.id2 = id2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }

    @Override
    public String toString() {
        return "User 1 ID=" + id1 +
                ", User 2 ID=" + id2;
    }

    public LocalDateTime getTime() {
        return friendsFrom;
    }

    public void setTime(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }
}
