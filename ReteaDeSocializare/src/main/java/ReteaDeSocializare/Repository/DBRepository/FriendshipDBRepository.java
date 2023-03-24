package ReteaDeSocializare.Repository.DBRepository;

import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.Validator;
import ReteaDeSocializare.Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FriendshipDBRepository implements  Repository<UUID, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Friendship findOne(UUID uuid) {
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Friendships\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                UUID id1 = (UUID) resultSet.getObject("id1");
                UUID id2 = (UUID) resultSet.getObject("id2");
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");
                UUID sentby = (UUID) resultSet.getObject("sentby");

                Friendship friendship = new Friendship(id, id1, id2, LocalDateTime.parse(date).toString(),status,sentby);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        String sql = "INSERT INTO \"Friendships\" (id, id1, id2, date, status, sentby ) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, entity.getId());
            ps.setObject(2, entity.getId1());
            ps.setObject(3, entity.getId2());
            ps.setString(4, entity.getTime().toString());
            ps.setString(5,entity.getStatus().toString());
            ps.setObject(6, entity.getSentby());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(UUID uuid) {
        String querry = "DELETE FROM \"Friendships\" WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(querry)) {
            statement.setObject(1, uuid);
            statement.executeUpdate();
            return null;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity, Friendship newentity) {
        String da = "Accepted";
        String querry = "UPDATE \"Friendships\" SET 'status' = ? WHERE 'id' = ?";
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             PreparedStatement statement = connection.prepareStatement(querry)) {
            statement.setObject(1, da);
            statement.setObject(2,entity.getId());
            statement.executeUpdate();
            return newentity;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
