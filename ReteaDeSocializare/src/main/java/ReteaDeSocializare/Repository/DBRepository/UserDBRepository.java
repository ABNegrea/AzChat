package ReteaDeSocializare.Repository.DBRepository;

import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.Validator;
import ReteaDeSocializare.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserDBRepository implements Repository<UUID, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(UUID uuid) {
        String querry = "SELECT * FROM \"Users\" WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(querry)) {
            statement.setObject(1, uuid);
            ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            statement.setObject(1, uuid);
            UUID id = (UUID) resultSet.getObject("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            User user = new User(id, firstName, lastName, password, email);
            return user;
        }
        }
         catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Users\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID uuid = (UUID) resultSet.getObject("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(uuid, firstName, lastName, password, email);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {

        String sql = "INSERT INTO \"Users\" (id, first_name, last_name, email, password ) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, entity.getId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4,entity.getEmail());
            ps.setString(5,entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(UUID uuid) {
            String querry = "DELETE FROM \"Users\" WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(querry)) {
            statement.setObject(1, uuid);
            User user = new User(findOne(uuid).getId(),findOne(uuid).getFirstName(),findOne(uuid).getLastName());
            statement.executeUpdate();
            return user;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User user, User newuser) {
        {
            String querry = "UPDATE \"Users\" SET id =?, first_name=?, last_name= ? WHERE id = ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(querry)) {
                statement.setObject(1, newuser.getId());
                statement.setString(2, newuser.getFirstName());
                statement.setString(3, newuser.getLastName());
                statement.setObject(4, user.getId());
                statement.executeUpdate();
                return newuser;
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
