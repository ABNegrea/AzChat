package ReteaDeSocializare.Domain;

import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private UUID id;
    private UUID sender;

    private UUID receiver;

    private Text message;

    private LocalDateTime sentat;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd YYYY hh:mm:ss");

    public Message(UUID id, UUID sender, UUID receiver, Text message) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.message.setWrappingWidth(100);
        this.sentat = LocalDateTime.now();
        dtf.format(sentat);
    }

    public Message(UUID id, UUID sender, UUID receiver, Text message, String date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.message.setWrappingWidth(100);
        this.sentat = LocalDateTime.parse(date);
        dtf.format(sentat);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getSentat() {
        return sentat;
    }

    public void setSentat(LocalDateTime sentat) {
        this.sentat = sentat;
    }

    public Message(UUID sender, UUID receiver, Text message) {
        this.setId(UUID.randomUUID());
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sentat = LocalDateTime.now();
        this.message.setWrappingWidth(100);
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text message) {
        this.message = message;
    }
}
