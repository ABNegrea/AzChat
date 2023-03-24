package ReteaDeSocializare.Service;
import ReteaDeSocializare.Domain.Exceptions.FriendshipException;
import ReteaDeSocializare.Domain.Exceptions.MessageException;
import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.Message;
import ReteaDeSocializare.Repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MessageService {
    private Repository<UUID, Message> messageRepo;

    public MessageService(Repository<UUID, Message> messageRepo) {
        this.messageRepo = messageRepo;
    }

    public ArrayList<Message> getAllMessages() {
        return new ArrayList<>((Collection<Message>) messageRepo.findAll());
    }

    public void addMessage(Message msg) {
        if( msg.getMessage().toString().equals(""))
            throw new MessageException("Empty message!\n");
        messageRepo.save(msg);
    }
}
