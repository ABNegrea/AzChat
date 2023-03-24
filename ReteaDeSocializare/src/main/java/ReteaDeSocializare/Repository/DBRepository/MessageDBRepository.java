package ReteaDeSocializare.Repository.DBRepository;

import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.Message;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.Validator;
import ReteaDeSocializare.Repository.Repository;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MessageDBRepository implements Repository<UUID, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;

    public MessageDBRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOne(UUID uuid) {
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Messages\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                UUID sender = (UUID) resultSet.getObject("sender");
                UUID receiver = (UUID) resultSet.getObject("receiver");
                Text text = new Text(resultSet.getString("message"));
                String date = resultSet.getString("sentat");

                Message message= new Message(id,sender,receiver,text,date);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        String sql = "INSERT INTO \"Messages\" (sender, receiver, message, id, sentat) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, entity.getSender());
            ps.setObject(2, entity.getReceiver());
            ps.setString(3, entity.getMessage().getText());
            ps.setObject(4, entity.getId());
            ps.setString(5, entity.getSentat().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(UUID uuid) {
        String querry = "DELETE FROM \"Messages\" WHERE id = ?";
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
    public Message update(Message entity, Message newEntity) {
        return null;
    }
}
